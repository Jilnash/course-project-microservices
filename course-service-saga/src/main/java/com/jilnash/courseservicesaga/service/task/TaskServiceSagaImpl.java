package com.jilnash.courseservicesaga.service.task;

import com.jilnash.courseservicedto.dto.task.*;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class TaskServiceSagaImpl implements TaskServiceSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;

    public TaskServiceSagaImpl(
            KafkaTemplate<String, Object> kafkaTemplate,
            ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    @Override
    public List<TaskResponse> getTasks(String courseId, String moduleId, String name) {
        kafkaTemplate.send("task-get-tasks-topic", new TasksRequestDTO(courseId, moduleId, name));
        return List.of();
    }

    @Override
    public TaskGraph getTasksAsGraph(String courseId, String moduleId) throws ExecutionException, InterruptedException {

//        ProducerRecord<String, Object> record = new ProducerRecord<>("task-get-tasks-as-graph-topic",
//                new TaskGraphRequestDTO(courseId, moduleId)
//        );
//
//        // Attach reply topic header
//        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "task-reply-topic".getBytes()));
//        RequestReplyFuture<String, Object, Object> future = replyingKafkaTemplate.sendAndReceive(record);
        return null; //(TaskGraph) future.get().value();
    }

    @Override
    public TaskResponse getTask(String id) {
        return null;
    }

    @Override
    public TaskResponse getTask(String courseId, String moduleId, String id) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(
                "task-get-task-by-course-module-id-topic",
                new TaskRequestDTO(courseId, moduleId, id)
        );
        // Attach reply topic header
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "task-reply-topic".getBytes()));
        record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes()));
        RequestReplyFuture<String, Object, Object> future = replyingKafkaTemplate.sendAndReceive(record);
        replyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10L)); // Set a timeout for the reply
        try {
            return (TaskResponse) future.get().value();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getTaskTitle(String id) {
        kafkaTemplate.send("task-get-title-topic", id);
        return "";
    }

    @Override
    public String getTaskDescription(String id) {
        kafkaTemplate.send("task-get-description-topic", id);
        return "";
    }

    @Override
    public String getTaskVideoFilePresignedUrl(String id) {
        kafkaTemplate.send("task-get-video-url-topic", id);
        return "";
    }

    @Override
    public Boolean getTaskIsPublic(String id) {
        kafkaTemplate.send("task-get-is-public-topic", id);
        return true;
    }

    @Override
    public void createTask(TaskCreateDTO taskCreateDTO) {

        //todo: upload file to file-service
        taskCreateDTO.setVideoFileName(taskCreateDTO.getVideoFileName());
//        taskCreateDTO.setVideoFile(null); // Remove the file from the DTO to avoid serialization issues
        kafkaTemplate.send("task-create-topic", taskCreateDTO);
    }

    @Override
    public void updateTaskTitle(String courseId, String moduleId, String taskId, String title) {
        kafkaTemplate.send("task-update-title-topic", new TaskUpdateTitleDTO(courseId, moduleId, taskId, title));
    }

    @Override
    public void updateTaskDescription(String courseId, String moduleId, String taskId, String description) {
        kafkaTemplate.send("task-update-description-topic", new TaskUpdateTitleDTO(courseId, moduleId, taskId, description));
    }

    @Override
    public void updateTaskVideoFile(String courseId, String moduleId, String id, MultipartFile videoFile) {
        kafkaTemplate.send("task-update-video-file-topic", new TaskUpdateVideoFileDTO(courseId, moduleId, id, videoFile.getOriginalFilename()));
    }

    @Override
    public void updateTaskIsPublic(String courseId, String moduleId, String id, Boolean isPublic) {
        kafkaTemplate.send("task-update-is-public-topic", new TaskUpdateIsPublicDTO(courseId, moduleId, id, isPublic));
    }

    @Override
    public void updateTaskHwPostingInterval(String courseId, String moduleId, String id, Integer hwPostingInterval) {
        kafkaTemplate.send("task-update-hw-posting-interval-topic", new TaskUpdateHwIntervalDTO(courseId, moduleId, id, hwPostingInterval));
    }

    @Override
    public void updateTaskPrerequisites(String courseId, String moduleId, String taskId, Set<String> prerequisiteTasksIds) {
        kafkaTemplate.send("task-update-prerequisites-topic", new TaskUpdatePrereqsDTO(courseId, moduleId, taskId, prerequisiteTasksIds));
    }

    @Override
    public void updateTaskSuccessors(String courseId, String moduleId, String taskId, Set<String> successorTasksIds) {
        kafkaTemplate.send("task-update-successors-topic", new TaskUpdateSuccessorsDTO(courseId, moduleId, taskId, successorTasksIds));
    }

    @Override
    public void softDeleteTask(String courseId, String moduleId, String taskId) {
        kafkaTemplate.send("task-soft-delete-topic", new TaskDeleteDTO(courseId, moduleId, taskId));
    }

    @Override
    public void hardDeleteTask(String courseId, String moduleId, String taskId) {
        kafkaTemplate.send("task-hard-delete-topic", new TaskDeleteDTO(courseId, moduleId, taskId));
    }
}
