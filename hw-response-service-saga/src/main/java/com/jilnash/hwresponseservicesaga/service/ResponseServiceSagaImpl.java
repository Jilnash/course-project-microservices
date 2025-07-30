package com.jilnash.hwresponseservicesaga.service;

import com.jilnash.courserightsservicedto.dto.CheckRightsDTO;
import com.jilnash.hwresponseservicedto.dto.ResponseCreateDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class ResponseServiceSagaImpl implements ResponseServiceSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ResponseServiceSagaImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void createResponse(ResponseCreateDTO response) {

        String transactionId = UUID.randomUUID().toString();
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, response.getCourseId(), response.getTeacherId(), Set.of()));

        kafkaTemplate.send("response-create-topic", response);
    }

    @Override
    public void updateResponse(ResponseCreateDTO response) {

        String transactionId = UUID.randomUUID().toString();
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, response.getCourseId(), response.getTeacherId(), Set.of()));

        kafkaTemplate.send("response-update-topic", response);
    }

    @Override
    public void softDeleteResponse(String courseId, String teacherId, String responseId) {

        String transactionId = UUID.randomUUID().toString();
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of()));

        kafkaTemplate.send("response-soft-delete-topic", responseId);
    }

    @Override
    public void hardDeleteResponse(String courseId, String teacherId, String responseId) {

        String transactionId = UUID.randomUUID().toString();
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of()));

        kafkaTemplate.send("response-hard-delete-topic", responseId);
    }
}
