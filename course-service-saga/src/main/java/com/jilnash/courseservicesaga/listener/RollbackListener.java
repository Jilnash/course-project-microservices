package com.jilnash.courseservicesaga.listener;

import com.jilnash.courseservicesaga.transaction.RollbackStage;
import com.jilnash.courseservicesaga.transaction.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RollbackListener {

    private final Map<String, Transaction> transactions;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public RollbackListener(Map<String, Transaction> transactions, KafkaTemplate<String, Object> kafkaTemplate) {
        this.transactions = transactions;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "rollback-topic", groupId = "course-service-saga-group")
    public void rollbackCourse(String transactionId) {
        for (RollbackStage stage : transactions.get(transactionId).getRollbackStages()) {
            kafkaTemplate.send(stage.getTopic(), stage.getPayload());
        }
    }
}
