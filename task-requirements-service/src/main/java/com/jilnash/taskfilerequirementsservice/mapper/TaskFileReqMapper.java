package com.jilnash.taskfilerequirementsservice.mapper;

import com.jilnash.taskfilerequirementsservice.dto.TaskFileReqDTO;
import com.jilnash.taskfilerequirementsservice.model.TaskFileRequirement;
import org.springframework.stereotype.Component;

@Component
public class TaskFileReqMapper {

    public static TaskFileReqDTO toDto(TaskFileRequirement taskFileRequirement) {
        return new TaskFileReqDTO(
                taskFileRequirement.getFileRequirement().getContentType(),
                taskFileRequirement.getCount()
        );
    }
}
