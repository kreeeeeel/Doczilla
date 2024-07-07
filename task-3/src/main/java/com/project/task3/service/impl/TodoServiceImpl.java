package com.project.task3.service.impl;

import com.project.task3.entity.TaskEntity;
import com.project.task3.service.TodoService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    private List<TaskEntity> tasks = new ArrayList<>(Arrays.asList(
            new TaskEntity(UUID.randomUUID(), "Обновление системы безопасности", "Установка последнего патча безопасности", "Необходимо загрузить и установить последнюю версию патча безопасности на все рабочие станции в офисе, чтобы предотвратить уязвимости.", new Date(), false),
            new TaskEntity(UUID.randomUUID(), "Подготовка презентации", "Презентация для клиента", "Подготовка подробной презентации с данными об эффективности проекта для встречи с новым потенциальным клиентом.", new Date(), true),
            new TaskEntity(UUID.randomUUID(), "Проведение тренинга", "Тренинг по использованию ПО", "Организовать тренинг для отдела продаж по новому программному обеспечению. Провести презентацию и дать ответы на вопросы сотрудников.", new Date(new Date().getTime() + (1000 * 60 * 60 * 24)), false),
            new TaskEntity(UUID.randomUUID(), "Резервное копирование данных", "Проверка резервного копирования", "Необходимо проверить и убедиться, что регулярное резервное копирование критически важных данных на сервере выполняется успешно.", new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * 3)), true)
    ));

    @Override
    public List<TaskEntity> getAll(Integer limit, Integer offset) {
        int start = (offset != null) ? offset : 0;
        int end = (limit != null) ? Math.min(start + limit, tasks.size()) : tasks.size();
        return tasks.subList(start, end);
    }

    @Override
    public List<TaskEntity> findByName(String name) {
        return tasks.stream()
                .filter(t -> t.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskEntity> getByDateRange(Date from, Date to) {
        return tasks.stream()
                .filter(t -> !t.getDate().before(from) && !t.getDate().after(to))
                .collect(Collectors.toList());
    }

    @Override
    public TaskEntity createTask(TaskEntity task) {
        task.setId(UUID.randomUUID());
        tasks.add(task);
        return task;
    }
}
