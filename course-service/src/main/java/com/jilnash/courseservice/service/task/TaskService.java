package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.dto.task.TaskResponseDTO;
import com.jilnash.courseservice.dto.task.TaskUpdateDTO;
import com.jilnash.courseservice.model.Task;

import java.util.List;

public interface TaskService {

    List<TaskResponseDTO> getTasks(String courseId, String moduleId, String name);

    Task getTask(String courseId, String moduleId, String id);

    Boolean create(TaskCreateDTO task);

    Task update(TaskUpdateDTO task);
}
