package com.project.task3.controller;

import com.project.task3.entity.TaskEntity;
import com.project.task3.service.TodoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<TaskEntity> getAll(@RequestParam(required = false) Integer limit, @RequestParam(required = false) Integer offset) {
        return todoService.getAll(limit, offset);
    }

    @GetMapping("/find")
    public List<TaskEntity> findByName(@RequestParam String q) {
        return todoService.findByName(q);
    }

    @GetMapping("/date")
    public List<TaskEntity> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to
    ) {
        return todoService.getByDateRange(from, to);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskEntity createTask(@RequestBody TaskEntity task) {
        return todoService.createTask(task);
    }
}
