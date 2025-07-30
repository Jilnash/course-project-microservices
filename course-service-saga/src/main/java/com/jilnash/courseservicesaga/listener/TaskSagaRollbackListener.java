package com.jilnash.courseservicesaga.listener;

import com.jilnash.courseservicedto.dto.task.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskSagaRollbackListener {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public TaskSagaRollbackListener(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "task-create-rollback-topic", groupId = "course-service-saga-group")
    public void rollbackTaskCreate(String courseId, String moduleId, String taskId) {

        kafkaTemplate.send("task-create-rollback-topic", taskId);
        //todo: rollback progress
        //todo: rollback file req creation
    }

    @KafkaListener(topics = "task-update-title-rollback-topic", groupId = "course-service-saga-group")
    public void rollbackTaskTitleUpdate(TaskUpdateTitleDTO dto) {
        kafkaTemplate.send("task-update-title-rollback-topic", dto);
    }

    @KafkaListener(topics = "task-update-description-rollback-topic", groupId = "course-service-saga-group")
    public void rollbackTaskDescriptionUpdate(TaskUpdateTitleDTO dto) {
        kafkaTemplate.send("task-update-description-rollback-topic", dto);
    }

    @KafkaListener(topics = "task-update-video-file-name-rollback-topic", groupId = "course-service-saga-group")
    public void rollbackTaskVideoFileNameUpdate(TaskUpdateVideoFileDTO dto) {
        kafkaTemplate.send("task-update-video-file-name-rollback-topic", dto);
    }

    @KafkaListener(topics = "task-update-is-public-rollback-topic", groupId = "course-service-saga-group")
    public void rollbackTaskIsPublicUpdate(TaskUpdateIsPublicDTO dto) {
        kafkaTemplate.send("task-update-is-public-rollback-topic", dto);
    }

    @KafkaListener(topics = "task-update-prerequisites-rollback-topic", groupId = "course-service-saga-group")
    public void rollbackTaskPrerequisitesUpdate(TaskUpdatePrereqsDTO dto) {
        kafkaTemplate.send("task-update-prerequisites-rollback-topic", dto);
        //todo: rollback progress
    }

    @KafkaListener(topics = "task-update-successors-rollback-topic", groupId = "course-service-saga-group")
    public void rollbackTaskSuccessorsUpdate(TaskUpdateSuccessorsDTO dto) {
        kafkaTemplate.send("task-update-successors-rollback-topic", dto);
        //todo: rollback progress
    }
}
