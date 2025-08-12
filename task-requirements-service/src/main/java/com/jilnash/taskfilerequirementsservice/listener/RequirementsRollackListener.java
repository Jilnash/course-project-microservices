package com.jilnash.taskfilerequirementsservice.listener;

import com.jilnash.taskfilerequirementsservice.service.TaskFileReqServiceRollback;
import com.jilnash.taskrequirementsservicedto.dto.SetRequirements;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RequirementsRollackListener {

    private final TaskFileReqServiceRollback taskFileReqServiceRollback;

    public RequirementsRollackListener(TaskFileReqServiceRollback taskFileReqServiceRollback) {
        this.taskFileReqServiceRollback = taskFileReqServiceRollback;
    }

    @KafkaListener(topics = "set-task-requirements-rollback-topic", groupId = "task-requirements-group")
    public void rollbackRequirements(SetRequirements dto) {

        taskFileReqServiceRollback.setTaskRequirementsRollback(dto.taskId(), dto.requirements());
    }
}
