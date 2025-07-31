package com.jilnash.taskfilerequirementsservice.listener;

import com.jilnash.taskfilerequirementsservice.service.TaskFileReqService;
import com.jilnash.taskrequirementsservicedto.dto.SetRequirements;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RequrementsListener {

    private final TaskFileReqService taskFileReqService;

    public RequrementsListener(TaskFileReqService taskFileReqService) {
        this.taskFileReqService = taskFileReqService;
    }

    @KafkaListener(topics = "set-task-requirements-topic", groupId = "task-requirements-group")
    public void setRequirements(SetRequirements dto) {

        taskFileReqService.setTaskRequirements(dto.taskId(), dto.requirements());
    }
}
