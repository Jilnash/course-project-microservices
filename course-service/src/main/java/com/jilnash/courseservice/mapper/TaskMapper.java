package com.jilnash.courseservice.mapper;

import com.jilnash.courseservice.dto.task.TaskCreateResponseDTO;
import com.jilnash.courseservice.dto.task.TaskResponseDTO;
import com.jilnash.courseservice.model.Task;
import org.springframework.stereotype.Component;

/**
 * Mapper class to handle the conversion between Task entities and the corresponding Data Transfer Objects (DTOs).
 * This class provides utility methods to convert between Task, TaskCreateDTO, TaskResponseDTO, and TaskCreateResponseDTO.
 */
@Component
public class TaskMapper {
    /**
     * Converts a Task entity into a TaskResponseDTO.
     *
     * @param task the Task entity to be converted
     * @return the TaskResponseDTO constructed based on the provided Task entity
     */
    public TaskResponseDTO toTaskResponse(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getVideoFileName(),
                task.getIsPublic(),
                task.getPrerequisites().stream().map(Task::getId).toList(),
                task.getSuccessors().stream().map(Task::getId).toList()
        );
    }

    /**
     * Converts a Task entity into a TaskCreateResponseDTO.
     *
     * @param task the Task entity to be converted
     * @return the TaskCreateResponseDTO constructed based on the provided Task entity
     */
    public TaskCreateResponseDTO toTaskCreateResponse(Task task) {
        return new TaskCreateResponseDTO(task.getId(), task.getTitle());
    }
}
