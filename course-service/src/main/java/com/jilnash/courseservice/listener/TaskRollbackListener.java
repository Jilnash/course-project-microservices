package com.jilnash.courseservice.listener;

import com.jilnash.courseservice.service.task.TaskServiceRollback;
import com.jilnash.courseservicedto.dto.task.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TaskRollbackListener {

    private final TaskServiceRollback taskServiceRollback;

    public TaskRollbackListener(TaskServiceRollback taskServiceRollback) {
        this.taskServiceRollback = taskServiceRollback;
    }

    @KafkaListener(topics = "task-create-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskCreate(TaskCreateDTO dto) {
        taskServiceRollback.rollbackTaskCreate(dto.getCourseId(), dto.getModuleId(), dto.getTaskId());
    }

    @KafkaListener(topics = "task-update-title-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskTitleUpdate(TaskUpdateTitleDTO dto) {
        taskServiceRollback.rollbackTaskTitleUpdate(dto.courseId(), dto.moduleId(), dto.taskId(), dto.title());
    }

    @KafkaListener(topics = "task-update-description-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskDescriptionUpdate(TaskUpdateDescriptionDTO dto) {
        taskServiceRollback.rollbackTaskDescriptionUpdate(dto.courseId(), dto.moduleId(), dto.taskId(), dto.description());
    }

    @KafkaListener(topics = "task-update-video-file-name-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskVideoFileNameUpdate(TaskUpdateVideoFileDTO dto) {
        taskServiceRollback.rollbackTaskVideoFileNameUpdate(dto.courseId(), dto.moduleId(), dto.id(), dto.videoFileName());
    }

    @KafkaListener(topics = "task-update-hw-posting-interval-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskHwPostingIntervalUpdate(TaskUpdateHwIntervalDTO dto) {
        taskServiceRollback.rollbackTaskPostingIntervalUpdate(dto.courseId(), dto.moduleId(), dto.taskId(), dto.hwPostingInterval());
    }

    @KafkaListener(topics = "task-update-is-public-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskIsPublicUpdate(TaskUpdateIsPublicDTO dto) {
        taskServiceRollback.rollbackTaskIsPublicUpdate(dto.courseId(), dto.moduleId(), dto.id(), dto.isPublic());
    }

    @KafkaListener(topics = "task-update-prerequisites-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskPrerequisitesUpdate(TaskUpdatePrereqsDTO dto) {
        taskServiceRollback.rollbackTaskPrerequisitesUpdate(dto.courseId(), dto.moduleId(), dto.taskId(), dto.prerequisiteTaskIds());
    }

    @KafkaListener(topics = "task-update-successors-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskSuccessorsUpdate(TaskUpdateSuccessorsDTO dto) {
        System.out.println(dto.successorTasksIds());
        taskServiceRollback.rollbackTaskSuccessorsUpdate(dto.courseId(), dto.moduleId(), dto.taskId(), dto.successorTasksIds());
    }

    @KafkaListener(topics = "task-soft-delete-rollback-topic", groupId = "course-service-group")
    public void rollbackTaskSoftDelete(TaskDeleteDTO dto) {
        taskServiceRollback.rollbackTaskSoftDelete(dto.courseId(), dto.moduleId(), dto.taskId());
    }
}
