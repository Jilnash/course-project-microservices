package com.jilnash.courseservicesaga.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RollbackListener {

    private final Set<String> transactionIds;

    public RollbackListener(Set<String> transactionIds) {
        this.transactionIds = transactionIds;
    }

    @KafkaListener(topics = "rollback-topic", groupId = "course-service-saga-group")
    public void rollbackCourse(String transactionId) {
        System.out.println(transactionIds);
        if (!transactionIds.contains(transactionId)) {
            System.out.println("Transaction ID not found: " + transactionId);
            return;
        }
        System.out.println("Rolling back course with transaction ID: " + transactionId);
    }
}
