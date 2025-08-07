package com.jilnash.hwresponseservice.listener;

import com.jilnash.hwresponseservice.service.HwResponseServiceRollback;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ResponseRollbackListener {

    private final HwResponseServiceRollback responseServiceRollback;

    public ResponseRollbackListener(HwResponseServiceRollback responseServiceRollback) {
        this.responseServiceRollback = responseServiceRollback;
    }

    @KafkaListener(topics = "response-create-rollback-topic", groupId = "hw-response-group")
    public void rollbackCreateResponse(String responseId) {
        responseServiceRollback.rollbackCreateResponse(responseId);
    }

    @KafkaListener(topics = "response-update-rollback-topic", groupId = "hw-response-group")
    public void rollbackUpdateResponse(String responseId) {
        responseServiceRollback.rollbackUpdateResponse(responseId);
    }

    @KafkaListener(topics = "response-soft-delete-rollback-topic", groupId = "hw-response-group")
    public void rollbackSoftDeleteResponse(String responseId) {
        responseServiceRollback.rollbackSoftDeleteResponse(responseId);
    }
}
