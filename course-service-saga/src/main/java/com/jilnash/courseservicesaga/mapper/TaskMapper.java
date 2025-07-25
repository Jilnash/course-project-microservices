package com.jilnash.courseservicesaga.mapper;

import com.jilnash.courseservicedto.dto.task.TaskCreateDTO;
import com.jilnash.courseservicesaga.dto.TaskSagaCreateDTO;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskCreateDTO toTaskCreateDTO(TaskSagaCreateDTO taskSagaCreateDTO) {
        return new TaskCreateDTO(
                taskSagaCreateDTO.getCourseId(),
                taskSagaCreateDTO.getModuleId(),
                taskSagaCreateDTO.getTaskId(),
                taskSagaCreateDTO.getAuthorId(),
                taskSagaCreateDTO.getTitle(),
                taskSagaCreateDTO.getDescription(),
                taskSagaCreateDTO.getVideoFile().getOriginalFilename(),
                taskSagaCreateDTO.getIsPublic(),
                taskSagaCreateDTO.getHwPostingInterval(),
                taskSagaCreateDTO.getPrerequisiteTasksIds(),
                taskSagaCreateDTO.getSuccessorTasksIds()
        );
    }
}
