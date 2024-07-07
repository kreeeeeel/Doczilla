let tasks = [];
let displayedTasks = [];
let statusMap = {};

$(document).ready(function () {
    getAllTasks();
});

function getAllTasks() {
    $.ajax({
        url: `http://localhost:8080/api/todos`,
        success: function (retrievedTasks) {
            updateTasks(retrievedTasks);
        }
    });
}

$(function () {
    $("#datepicker").datepicker({
        dateFormat: "dd-mm-yy",
        onSelect: function (dateText) {
            getTasksByDate(dateText);
        }
    });
});

function getTodayTasks() {
    const today = new Date();
    const formattedToday = formatDate(today);
    filterTasksByDate(formattedToday);
}

function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

function filterTasksByDate(targetDate) {
    const filteredTasks = tasks.filter(task => {
        const taskDate = task.date.slice(0, 10);
        return taskDate === targetDate;
    });
    renderTasks(filteredTasks);
}

function getWeekTasks() {
    const today = new Date();
    const dayOfWeek = today.getDay() || 7;
    const monday = formatDate(new Date(today.setDate(today.getDate() - dayOfWeek + 1)));
    const sunday = formatDate(new Date(today.setDate(today.getDate() - today.getDay() + 7)));
    getTasksInRange(monday, sunday);
}

function getTasksByDate(dateText) {
    const [day, month, year] = dateText.split('-');
    const formattedDate = `${year}-${month}-${day}`;

    const filteredTasks = tasks.filter(task => {
        const taskDate = new Date(task.date).toISOString().slice(0, 10);
        return taskDate === formattedDate;
    });

    renderTasks(filteredTasks);
}

function getTasksInRange(fromDate, toDate) {
    $.ajax({
        url: `http://localhost:8080/api/todos/date?from=${fromDate}&to=${toDate}`,
        success: function(retrievedTasks) {
            updateTasks(retrievedTasks);
        }
    });
}

function updateTasks(retrievedTasks) {
    tasks = retrievedTasks.map(task => ({
        ...task,
        status: statusMap[task.id] !== undefined ? statusMap[task.id] : task.status
    }));
    displayedTasks = [...tasks];
    renderTasks(displayedTasks);
}

function toggleTaskStatus(taskId) {
    const task = tasks.find(t => t.id === taskId);
    if (task) {
        task.status = !task.status;
        statusMap[task.id] = task.status;
        renderTasks(displayedTasks);
    }
}

function renderTasks(taskArray) {
    const taskList = $('.task-list');
    taskList.empty();
    if (taskArray.length === 0) {
        taskList.append(`<p>Задачи за выбранную дату отсутствуют.</p>`);
    } else {
        taskArray.forEach(task => {
            taskList.append(`
        <div class="task-item">
            <div class="task-item-content">
                <h3>${task.name}</h3>
                <p>${task.shortDesc}</p>
                <p>${new Date(task.date).toLocaleDateString()}</p>
            </div>
            <input class="checkbox-task" type="checkbox" onclick="toggleTaskStatus('${task.id}')" ${task.status ? 'checked' : ''}>
            <button class="btn-view-task" data-id="${task.id}" onclick="showTaskDetails('${task.id}')">Подробнее</button>
        </div>
    `);
        });
    }
}

function showTaskDetails(taskId) {
    const task = tasks.find(t => t.id === taskId);
    if (task) {
        const taskDetails = $('.modal');
        taskDetails.html(`
            <h2>${task.name}</h2>
            <p>${new Date(task.date).toLocaleDateString()}</p>
            <p>${task.shortDesc}</p>
            <p>${task.fullDesc}</p>
            <button class="btn-close" onclick="closeModal()">Готово</button>
        `);
        $('.overlay').show();
        taskDetails.show();
    }
}

function closeModal() {
    $('.modal').hide();
    $('.overlay').hide();
}

function toggleIncomplete(checked) {
    if (checked) {
        displayedTasks = tasks.filter(t => !t.status);
    } else {
        displayedTasks = [...tasks];
    }
    renderTasks(displayedTasks);
}
