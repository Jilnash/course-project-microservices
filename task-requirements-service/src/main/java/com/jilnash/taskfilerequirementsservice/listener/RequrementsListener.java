package com.jilnash.taskfilerequirementsservice.listener;

import com.jilnash.taskfilerequirementsservice.service.TaskFileReqService;
import com.jilnash.taskrequirementsservicedto.dto.CheckOfRequirements;
import com.jilnash.taskrequirementsservicedto.dto.SetRequirements;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class RequrementsListener {

    private final TaskFileReqService taskFileReqService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public RequrementsListener(TaskFileReqService taskFileReqService,
                               KafkaTemplate<String, String> kafkaTemplate) {
        this.taskFileReqService = taskFileReqService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "set-task-requirements-topic", groupId = "task-requirements-group")
    public void setRequirements(SetRequirements dto) {
        taskFileReqService.setTaskRequirements(dto.taskId(), dto.requirements());
    }

    @KafkaListener(topics = "check-hw-requirements-topic", groupId = "task-requirements-group")
    public void checkRequirements(CheckOfRequirements dto) {
        if (!taskFileReqService.checkOfRequirements(dto.taskId(), dto.contentTypes()))
            kafkaTemplate.send("rollback-topic", dto.transactionId());
    }
}
