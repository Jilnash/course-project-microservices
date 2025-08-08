package com.jilnash.homeworkservice.listener;

import com.jilnash.homeworkservice.service.HomeworkServiceRollback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class HomeworkRollbackListener {

    private final HomeworkServiceRollback homeworkServiceRollback;

    public HomeworkRollbackListener(HomeworkServiceRollback homeworkServiceRollback) {
        this.homeworkServiceRollback = homeworkServiceRollback;
    }

    @KafkaListener(topics = "homework-create-rollback-topic", groupId = "homework-service")
    public void handleHomeworkCreateRollback(String homeworkId) {

        log.info("Received rollback request for homework creation with ID: {}", homeworkId);
        homeworkServiceRollback.homeworkCreateRollback(UUID.fromString(homeworkId));
    }

    @KafkaListener(topics = "homework-checked-rollback-topic", groupId = "homework-service")
    public void handleHomeworkCheckedRollback(UUID homeworkId) {

        log.info("Received rollback request for homework checked with ID: {}", homeworkId);
        homeworkServiceRollback.homeworkCheckedRollback(homeworkId);
    }

    @KafkaListener(topics = "homework-soft-delete-rollback-topic", groupId = "homework-service")
    public void handleSoftDeleteHomeworkRollback(UUID homeworkId) {

        log.info("Received rollback request for soft delete of homework with ID: {}", homeworkId);
        homeworkServiceRollback.softDeleteHomeworkRollack(homeworkId);
    }
}
