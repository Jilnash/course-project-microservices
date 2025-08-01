package com.jilnash.progressservice.listener;

import com.jilnash.progressservice.service.TaskCompleteService;
import com.jilnash.progressservicedto.dto.InsertTaskToProgressDTO;
import com.jilnash.progressservicedto.dto.RemoveTasksFromProgressDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TaskCompleteListener {

    private final TaskCompleteService taskCompleteService;

    public TaskCompleteListener(TaskCompleteService taskCompleteService) {
        this.taskCompleteService = taskCompleteService;
    }

    @KafkaListener(topics = "insert-task-progress-topic", groupId = "progress-service")
    public void handleInsertTask(InsertTaskToProgressDTO dto) {
        taskCompleteService.insertTask(dto.taskId(), dto.prerequisites());
    }

    @KafkaListener(topics = "soft-delete-progress-topic", groupId = "progress-service")
    public void handleSoftDelete(RemoveTasksFromProgressDTO dto) {
        taskCompleteService.softDeleteTasks(dto.taskIds());
    }

    @KafkaListener(topics = "hard-delete-progress-topic", groupId = "progress-service")
    public void handleHardDelete(RemoveTasksFromProgressDTO dto) {
        taskCompleteService.hardDeleteTasks(dto.taskIds());
    }
}
