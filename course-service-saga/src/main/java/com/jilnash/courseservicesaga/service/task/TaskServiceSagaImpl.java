package com.jilnash.courseservicesaga.service.task;

import com.jilnash.courserightsservicedto.dto.CheckRightsDTO;
import com.jilnash.courseservicedto.dto.task.*;
import com.jilnash.courseservicesaga.client.FileServiceClient;
import com.jilnash.courseservicesaga.dto.TaskSagaCreateDTO;
import com.jilnash.courseservicesaga.mapper.TaskMapper;
import com.jilnash.progressservicedto.dto.InsertTaskToProgressDTO;
import com.jilnash.progressservicedto.dto.RemoveTasksFromProgressDTO;
import com.jilnash.taskrequirementsservicedto.dto.SetRequirements;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class TaskServiceSagaImpl implements TaskServiceSaga {

    private final TaskMapper taskMapper;

    private final HttpServletRequest request;

    private final FileServiceClient fileServiceClient;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;

    @Override
    public List<TaskResponse> getTasks(String userId, String courseId, String moduleId, String name) {

        kafkaTemplate.send("task-get-tasks-topic", new TasksRequestDTO(courseId, moduleId, name));
        return List.of();
    }

    @Override
    public TaskGraph getTasksAsGraph(String courseId, String moduleId) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(
                "task-get-tasks-as-graph-topic",
                new TaskGraphRequestDTO(courseId, moduleId)
        );

        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "task-graph-reply-topic".getBytes()));
        record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes()));

        RequestReplyFuture<String, Object, Object> future = replyingKafkaTemplate.sendAndReceive(record);
        replyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofMinutes(1L));

        try {
            return (TaskGraph) future.get().value();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to retrieve TaskGraph", e);
        }
    }

    @Override
    public TaskResponse getTask(String userId, String id) {
        ProducerRecord<String, Object> record = new ProducerRecord<>("task-get-task-topic", id);

        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "task-reply-topic".getBytes()));
        record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes()));

        RequestReplyFuture<String, Object, Object> future = replyingKafkaTemplate.sendAndReceive(record);
        replyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10L));

        try {
            return (TaskResponse) future.get().value();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to retrieve Task with id: " + id, e);
        }
    }

    @Override
    public TaskResponse getTask(String userId, String courseId, String moduleId, String id) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(
                "task-get-task-by-course-module-id-topic",
                new TaskRequestDTO(courseId, moduleId, id)
        );

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
    public String getTaskTitle(String userId, String id) {
        ProducerRecord<String, Object> record = new ProducerRecord<>("task-get-title-topic", id);

        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "task-title-reply-topic".getBytes()));
        record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes()));

        RequestReplyFuture<String, Object, Object> future = replyingKafkaTemplate.sendAndReceive(record);

        try {
            return future.get().value().toString();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getTaskDescription(String userId, String id) {
        ProducerRecord<String, Object> record = new ProducerRecord<>("task-get-description-topic", id);

        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "task-description-reply-topic".getBytes()));
        record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes()));

        RequestReplyFuture<String, Object, Object> future = replyingKafkaTemplate.sendAndReceive(record);
        replyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10L));

        try {
            return (String) future.get().value();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to retrieve Task description for id: " + id, e);
        }
    }

    @Override
    public String getTaskVideoFilePresignedUrl(String userId, String id) {
        ProducerRecord<String, Object> record = new ProducerRecord<>("task-get-video-url-topic", id);

        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "task-video-url-reply-topic".getBytes()));
        record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes()));

        RequestReplyFuture<String, Object, Object> future = replyingKafkaTemplate.sendAndReceive(record);
        replyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10L));

        try {
            return (String) future.get().value();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to retrieve video URL for Task id: " + id, e);
        }
    }

    @Override
    public Boolean getTaskIsPublic(String userId, String id) {
        ProducerRecord<String, Object> record = new ProducerRecord<>("task-get-is-public-topic", id);

        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "task-is-public-reply-topic".getBytes()));
        record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes()));

        RequestReplyFuture<String, Object, Object> future = replyingKafkaTemplate.sendAndReceive(record);
        replyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10L));

        try {
            return (Boolean) future.get().value();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to retrieve public status for Task id: " + id, e);
        }
    }

    @Override
    public void createTask(TaskSagaCreateDTO dto) {
        String transactionId = request.getHeader("X-Transaction-Id");

        List<String> taskIds = new LinkedList<>(dto.getPrerequisiteTasksIds());
        taskIds.addAll(dto.getSuccessorTasksIds());

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, dto.getCourseId(), dto.getAuthorId(), Set.of("CREATE")));

        kafkaTemplate.send("insert-task-progress-topic",
                new InsertTaskToProgressDTO(transactionId, dto.getTaskId(), taskIds));

        kafkaTemplate.send("set-task-requirements-topic",
                new SetRequirements(transactionId, dto.getTaskId(), dto.getReqirements()));

        kafkaTemplate.send("task-create-topic", taskMapper.toTaskCreateDTO(dto));

        fileServiceClient.uploadFileAsync(dto.getTaskId(), dto.getVideoFile());
    }

    @Override
    public void updateTaskTitle(String teacherId, String courseId, String moduleId, String taskId, String title) {

        String transactionId = request.getHeader("X-Transaction-Id");
//        String teacherId = request.getHeader("X-User-Sub");

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("UPDATE")));

        kafkaTemplate.send("task-update-title-topic", new TaskUpdateTitleDTO(courseId, moduleId, taskId, title));
    }

    @Override
    public void updateTaskDescription(String teacherId, String courseId, String moduleId, String taskId, String description) {

        String transactionId = request.getHeader("X-Transaction-Id");
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("UPDATE")));

        kafkaTemplate.send("task-update-description-topic", new TaskUpdateDescriptionDTO(courseId, moduleId, taskId, description));
    }

    @Override
    public void updateTaskVideoFile(String teacherId, String courseId, String moduleId, String id, MultipartFile videoFile) {

        String transactionId = request.getHeader("X-Transaction-Id");
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("UPDATE")));

        kafkaTemplate.send("task-update-video-file-topic",
                new TaskUpdateVideoFileDTO(courseId, moduleId, id, videoFile.getOriginalFilename()));

        fileServiceClient.updateFile(id, videoFile);
    }

    @Override
    public void updateTaskIsPublic(String teacherId, String courseId, String moduleId, String id, Boolean isPublic) {

        String transactionId = request.getHeader("X-Transaction-Id");
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("UPDATE")));

        kafkaTemplate.send("task-update-is-public-topic", new TaskUpdateIsPublicDTO(courseId, moduleId, id, isPublic));
    }

    @Override
    public void updateTaskHwPostingInterval(String teacherId, String courseId, String moduleId, String id, Integer hwPostingInterval) {

        String transactionId = request.getHeader("X-Transaction-Id");
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("UPDATE")));

        kafkaTemplate.send("task-update-hw-posting-interval-topic", new TaskUpdateHwIntervalDTO(courseId, moduleId, id, hwPostingInterval));
    }

    @Override
    public void updateTaskPrerequisites(String teacherId, String courseId, String moduleId, String taskId, Set<String> prerequisiteTasksIds) {

        String transactionId = request.getHeader("X-Transaction-Id");
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("UPDATE")));

        kafkaTemplate.send("task-update-prerequisites-topic", new TaskUpdatePrereqsDTO(courseId, moduleId, taskId, prerequisiteTasksIds));
        //todo: update progress
    }

    @Override
    public void updateTaskSuccessors(String teacherId, String courseId, String moduleId, String taskId, Set<String> successorTasksIds) {

        String transactionId = request.getHeader("X-Transaction-Id");
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("UPDATE")));

        kafkaTemplate.send("task-update-successors-topic",
                new TaskUpdateSuccessorsDTO(courseId, moduleId, taskId, successorTasksIds));
        //todo: update progress
    }

    @Override
    public void softDeleteTask(String teacherId, String courseId, String moduleId, String taskId) {

        String transactionId = request.getHeader("X-Transaction-Id");
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("DELETE")));
        kafkaTemplate.send("soft-delete-progress-topic",
                new RemoveTasksFromProgressDTO(transactionId, List.of(taskId)));
        kafkaTemplate.send("task-soft-delete-topic", new TaskDeleteDTO(courseId, moduleId, taskId));
    }

    @Override
    public void hardDeleteTask(String teacherId, String courseId, String moduleId, String taskId) {

        String transactionId = request.getHeader("X-Transaction-Id");
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("DELETE")));
        kafkaTemplate.send("hard-delete-progress-topic",
                new RemoveTasksFromProgressDTO(transactionId, List.of(taskId)));
        kafkaTemplate.send("task-hard-delete-topic", new TaskDeleteDTO(courseId, moduleId, taskId));
    }
}
