package com.jilnash.courseservice.mapper;

import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.dto.task.TaskResponseDTO;
import com.jilnash.courseservice.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public static Task toNode(TaskCreateDTO taskCreateDTO) {
        return Task.builder()
                .title(taskCreateDTO.getTitle())
                .description(taskCreateDTO.getDescription())
                .videoLink(taskCreateDTO.getVideoLink())
                .module(taskCreateDTO.getModule())
                .build();
    }

    public static TaskResponseDTO toTaskResponse(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getVideoLink(),
                task.getPrerequisites().stream().map(Task::getId).toList(),
                task.getSuccessors().stream().map(Task::getId).toList()
        );
    }
}
