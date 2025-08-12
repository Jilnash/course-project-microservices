package com.jilnash.progressservice.listener;

import com.jilnash.progressservice.service.rollback.TaskCompleteServiceRollback;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskCompleteRollackListener {

    private final TaskCompleteServiceRollback taskCompleteServiceRollback;

    public TaskCompleteRollackListener(TaskCompleteServiceRollback taskCompleteServiceRollback) {
        this.taskCompleteServiceRollback = taskCompleteServiceRollback;
    }

    @KafkaListener(topics = "insert-task-progress-rollback-topic", groupId = "progress-service")
    public void handleInsertTaskRollback(String taskId) {
        taskCompleteServiceRollback.insertTaskRollback(taskId);
    }

    @KafkaListener(topics = "soft-delete-progress-rollback-topic", groupId = "progress-service")
    public void handleSoftDeleteRollback(List<String> taskIds) {
        taskCompleteServiceRollback.softDeleteTasksRollback(taskIds);
    }
}
