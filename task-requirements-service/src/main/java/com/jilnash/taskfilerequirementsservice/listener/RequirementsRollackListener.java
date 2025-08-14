package com.jilnash.taskfilerequirementsservice.listener;

import com.jilnash.taskfilerequirementsservice.service.TaskFileReqServiceRollback;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RequirementsRollackListener {

    private final TaskFileReqServiceRollback taskFileReqServiceRollback;

    public RequirementsRollackListener(TaskFileReqServiceRollback taskFileReqServiceRollback) {
        this.taskFileReqServiceRollback = taskFileReqServiceRollback;
    }

    @KafkaListener(topics = "create-task-requirements-rollback-topic", groupId = "task-requirements-group")
    public void rollbackRequirements(String taskId) {

        taskFileReqServiceRollback.createTaskRequirementsRollback(taskId);
    }
}
