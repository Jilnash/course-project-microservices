package com.jilnash.taskfilerequirementsservice.mapper;

import com.jilnash.taskfilerequirementsservice.model.TaskFileRequirement;
import com.jilnash.taskrequirementsservicedto.dto.FileReqirement;
import org.springframework.stereotype.Component;

@Component
public class TaskFileReqMapper {

    public static FileReqirement toDto(TaskFileRequirement taskFileRequirement) {
        return new FileReqirement(
                taskFileRequirement.getFileRequirement().getContentType(),
                taskFileRequirement.getCount()
        );
    }
}
