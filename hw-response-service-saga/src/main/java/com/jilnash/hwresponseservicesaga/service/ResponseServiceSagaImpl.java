package com.jilnash.hwresponseservicesaga.service;

import com.jilnash.hwresponseservicedto.dto.ResponseCreateDTO;
import org.springframework.kafka.core.KafkaTemplate;

public class ResponseServiceSagaImpl implements ResponseServiceSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ResponseServiceSagaImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void createResponse(ResponseCreateDTO response) {
        //todo: check teacher permission
        kafkaTemplate.send("response-create-topic", response);
    }

    @Override
    public void updateResponse(ResponseCreateDTO response) {
        //todo: check teacher permission
        kafkaTemplate.send("response-update-topic", response);
    }

    @Override
    public void softDeleteResponse(String teacherId, String responseId) {
        //todo: check teacher permission
        kafkaTemplate.send("response-soft-delete-topic", responseId);
    }

    @Override
    public void hardDeleteResponse(String teacherId, String responseId) {
        //todo: check teacher permission
        kafkaTemplate.send("response-hard-delete-topic", responseId);
    }
}
