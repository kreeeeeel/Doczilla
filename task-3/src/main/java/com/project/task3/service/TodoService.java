package com.project.task3.service;

import com.project.task3.entity.TaskEntity;

import java.util.Date;
import java.util.List;

public interface TodoService {
    List<TaskEntity> getAll(Integer limit, Integer offset);
    List<TaskEntity> findByName(String name);
    List<TaskEntity> getByDateRange(Date from, Date to);
    TaskEntity createTask(TaskEntity task);
}
