package com.jilnash.courseservice.listener;

import com.jilnash.courseservice.service.task.TaskServiceRollback;
import com.jilnash.courseservicedto.dto.task.TaskRollbackDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TaskRollbackListener {

    private final TaskServiceRollback taskServiceRollback;

    public TaskRollbackListener(TaskServiceRollback taskServiceRollback) {
        this.taskServiceRollback = taskServiceRollback;
    }

    @KafkaListener(topics = "task-create-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskCreate(TaskRollbackDTO dto) {
        taskServiceRollback.rollbackTaskCreate(dto.courseId(), dto.moduleId(), dto.taskId());
    }

    @KafkaListener(topics = "task-update-title-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskTitleUpdate(TaskRollbackDTO dto) {
        taskServiceRollback.rollbackTaskTitleUpdate(dto.courseId(), dto.moduleId(), dto.taskId());
    }

    @KafkaListener(topics = "task-update-description-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskDescriptionUpdate(TaskRollbackDTO dto) {
        taskServiceRollback.rollbackTaskDescriptionUpdate(dto.courseId(), dto.moduleId(), dto.taskId());
    }

    @KafkaListener(topics = "task-update-video-file-name-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskVideoFileNameUpdate(TaskRollbackDTO dto) {
        taskServiceRollback.rollbackTaskVideoFileNameUpdate(dto.courseId(), dto.moduleId(), dto.taskId());
    }

    @KafkaListener(topics = "task-update-hw-posting-interval-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskHwPostingIntervalUpdate(TaskRollbackDTO dto) {
        taskServiceRollback.rollbackTaskPostingIntervalUpdate(dto.courseId(), dto.moduleId(), dto.taskId());
    }

    @KafkaListener(topics = "task-update-is-public-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskIsPublicUpdate(TaskRollbackDTO dto) {
        taskServiceRollback.rollbackTaskIsPublicUpdate(dto.courseId(), dto.moduleId(), dto.taskId());
    }

    @KafkaListener(topics = "task-update-prerequisites-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskPrerequisitesUpdate(String taskId) {
        taskServiceRollback.rollbackTaskPrerequisitesUpdate(taskId);
    }

    @KafkaListener(topics = "task-update-successors-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskSuccessorsUpdate(String taskId) {
        taskServiceRollback.rollbackTaskSuccessorsUpdate(taskId);
    }

    @KafkaListener(topics = "task-soft-delete-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskSoftDelete(TaskRollbackDTO dto) {
        taskServiceRollback.rollbackTaskSoftDelete(dto.courseId(), dto.moduleId(), dto.taskId());
    }
}
