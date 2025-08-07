package com.jilnash.hwresponseservicesaga.listener;

import com.jilnash.hwresponseservicesaga.transaction.RollbackStage;
import com.jilnash.hwresponseservicesaga.transaction.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RollbackListener {

    private final Map<String, Transaction> transactionsMap;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public RollbackListener(Map<String, Transaction> transactionsMap,
                            KafkaTemplate<String, Object> kafkaTemplate) {
        this.transactionsMap = transactionsMap;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "rollback-topic", groupId = "response-service-saga-group")
    public void handleRollback(String transactionId) {

        if (!transactionsMap.containsKey(transactionId))
            return;

        for (RollbackStage stage : transactionsMap.get(transactionId).getRollbackStages())
            kafkaTemplate.send(stage.getTopic(), stage.getPayload());
    }
}
