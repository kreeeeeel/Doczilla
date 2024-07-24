const server = "http://localhost:8080/Gradle___com_project___task_2_1_0_SNAPSHOT_war/api/student";

const modal = $('#studentModal');
modal.hide();

$('#openModal').on('click', function () {
    modal.show();
});
$('.close').on('click', function () {
    modal.hide();
});
$(window).on('click', function (event) {
    if (event.target.id === 'studentModal') {
        modal.hide();
    }
});

function formatDateToDDMMYYYY(dateStr) {
    var parsedDate = moment(dateStr, "YYYY-MM-DD", true);
    if (!parsedDate.isValid()) {
        alert("Неверный формат даты.");
        return null;
    }
    return parsedDate.format("DD-MM-YYYY");
}

const monthNames = ["января", "февраля", "марта", "апреля", "мая", "июня",
    "июля", "августа", "сентября", "октября", "ноября", "декабря"];

function formatDateWithOneBasedMonths(dateStr) {
    const dateObj = new Date(dateStr);
    if (isNaN(dateObj.getTime())) {
        console.error("Неверная строка даты:", dateStr);
        return "";
    }
    const day = dateObj.getDate();
    const monthIndex = dateObj.getMonth();
    const year = dateObj.getFullYear();
    const monthName = monthNames[monthIndex];
    return `${day} ${monthName} ${year} г.`;
}

function loadStudents() {
    $.ajax({
        url: server,
        type: 'GET',
        success: function (data) {
            var tableBody = $('#studentsTable tbody');
            tableBody.empty();
            $.each(data, function (index, student) {
                var row = $('<tr>');
                row.append($('<td>').text(student.id));
                row.append($('<td>').text(student.firstName));
                row.append($('<td>').text(student.lastName));
                row.append($('<td>').text(student.middleName || ''));
                row.append($('<td>').text(formatDateWithOneBasedMonths(student.birthDate)));
                row.append($('<td>').text(student.group));

                var deleteCross = $('<span>')
                    .text('✖')
                    .addClass('delete-cross')
                    .click(function () {
                        deleteStudent(student.id);
                    });
                row.append(deleteCross);

                tableBody.append(row);
            });
        },
        error: function () {
            alert('Ошибка загрузки данных студентов.');
        }
    });
}

$('#studentForm').on('submit', function (e) {
    e.preventDefault();
    var birthDateStr = $('#birthDate').val();
    var formattedDate = formatDateToDDMMYYYY(birthDateStr);
    if (!formattedDate) return;
    var formData = {
        firstName: $('#firstName').val(),
        lastName: $('#lastName').val(),
        middleName: $('#middleName').val() || '',
        birthDate: formattedDate,
        group: $('#group').val()
    };

    $.ajax({
        url: server,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function () {
            loadStudents();
            modal.hide();
        },
        error: function () {
            alert('Ошибка добавления студента.');
        }
    });
});

function deleteStudent(studentId) {
    $.ajax({
        url: server + '?id=' + studentId,
        type: 'DELETE',
        success: function () {
            loadStudents();
        },
        error: function () {
            alert('Ошибка удаления студента.');
        }
    });
}

loadStudents();
