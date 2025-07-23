package com.jilnash.courseservice.mapper;

import com.jilnash.courseservice.model.Task;
import com.jilnash.courseservicedto.dto.task.TaskResponse;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponse toTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getVideoFileName(),
                task.getIsPublic(),
                task.getPrerequisites().stream().map(Task::getId).toList(),
                task.getSuccessors().stream().map(Task::getId).toList()
        );
    }
}
