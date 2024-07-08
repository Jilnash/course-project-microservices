package com.jilnash.courseservice.service;

import com.jilnash.courseservice.model.Task;

import java.util.List;

public interface TaskService {

    List<Task> getTasks(String title, Long courseId, Long moduleId);

    Task getTask(Long id, Long moduleId, Long courseId);

    Task save(Task task);
}
