package com.jilnash.courseservicesaga.service.task;

import com.jilnash.courseservicedto.dto.task.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Component
public class TaskServiceRollbackSagaImpl implements TaskServiceRollbackSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public TaskServiceRollbackSagaImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void rollbackTaskCreate(String courseId, String moduleId, String taskId) {
        kafkaTemplate.send("task-create-rollback-topic", taskId);
    }

    @Override
    public void rollbackTaskTitleUpdate(String courseId, String moduleId, String taskId, String oldTitle) {
        kafkaTemplate.send("task-update-title-rollback-topic",
                new TaskUpdateTitleDTO(courseId, moduleId, taskId, oldTitle));
    }

    @Override
    public void rollbackTaskDescriptionUpdate(String courseId, String moduleId, String taskId, String oldDescription) {
        kafkaTemplate.send("task-update-description-rollback-topic",
                new TaskUpdateDescriptionDTO(courseId, moduleId, taskId, oldDescription));
    }

    @Override
    public void rollbackTaskVideoFileUpdate(String courseId, String moduleId, String taskId, MultipartFile oldVideoFile) {
        kafkaTemplate.send("task-update-video-file-name-rollback-topic",
                new TaskUpdateVideoFileDTO(courseId, moduleId, taskId, oldVideoFile.getOriginalFilename()));
    }

    @Override
    public void rollbackTaskIsPublicUpdate(String courseId, String moduleId, String taskId, boolean oldIsPublic) {
        kafkaTemplate.send("task-update-is-public-rollback-topic",
                new TaskUpdateIsPublicDTO(courseId, moduleId, taskId, oldIsPublic));
    }

    @Override
    public void rollbackTaskPrerequisitesUpdate(String courseId, String moduleId, String taskId, Set<String> oldPrerequisites) {
        kafkaTemplate.send("task-update-prerequisites-rollback-topic",
                new TaskUpdatePrereqsDTO(courseId, moduleId, taskId, oldPrerequisites));
    }

    @Override
    public void rollbackTaskSuccessorsUpdate(String courseId, String moduleId, String taskId, Set<String> oldSuccessors) {
        kafkaTemplate.send("task-update-successors-rollback-topic",
                new TaskUpdateSuccessorsDTO(courseId, moduleId, taskId, oldSuccessors));
    }

    @Override
    public void rollbackTaskSoftDelete(String courseId, String moduleId, String taskId) {
        kafkaTemplate.send("task-soft-delete-rollback-topic",
                new TaskDeleteDTO(courseId, moduleId, taskId));
    }
}
