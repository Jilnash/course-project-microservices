package com.jilnash.hwservicesaga.listener;

import com.jilnash.hwservicesaga.transaction.RollbackStage;
import com.jilnash.hwservicesaga.transaction.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RollbackListener {

    private final Map<String, Transaction> transactionMap;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public RollbackListener(Map<String, Transaction> transactionMap,
                            KafkaTemplate<String, Object> kafkaTemplate) {
        this.transactionMap = transactionMap;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "rollback-topic", groupId = "hw-service-saga-group")
    public void rollbackTransaction(String transactionId) {

        if (!transactionMap.containsKey(transactionId))
            return;

        for (RollbackStage stage : transactionMap.get(transactionId).getRollbackStages())
            kafkaTemplate.send(stage.getTopic(), stage.getPayload());
    }
}
