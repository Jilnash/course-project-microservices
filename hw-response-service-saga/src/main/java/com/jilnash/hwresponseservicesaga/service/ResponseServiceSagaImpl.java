package com.jilnash.hwresponseservicesaga.service;

import com.jilnash.courserightsservicedto.dto.CheckRightsDTO;
import com.jilnash.hwresponseservicedto.dto.ResponseCreateDTO;
import com.jilnash.progressservicedto.dto.AddStudentTaskCompleteDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class ResponseServiceSagaImpl implements ResponseServiceSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final HttpServletRequest request;

    public ResponseServiceSagaImpl(KafkaTemplate<String, Object> kafkaTemplate, HttpServletRequest request) {
        this.kafkaTemplate = kafkaTemplate;
        this.request = request;
    }

    @Override
    public void createResponse(ResponseCreateDTO response) {

        String transactionId = request.getHeader("X-Transaction-Id");
        response.setTransactionId(transactionId);

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, response.getCourseId(), response.getTeacherId(), Set.of("RESPOND")));

        kafkaTemplate.send("homework-checked-topic", UUID.fromString(response.getHwId()));
        kafkaTemplate.send("response-create-topic", response);
        if (response.getIsCorrect())
            kafkaTemplate.send("add-student-task-complete-topic",
                    new AddStudentTaskCompleteDTO(transactionId, response.getStudentId(), response.getTaskId()));
    }

    @Override
    public void updateResponse(ResponseCreateDTO response) {

        String transactionId = request.getHeader("X-Transaction-Id");
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, response.getCourseId(), response.getTeacherId(), Set.of("RESPOND")));

        kafkaTemplate.send("response-update-topic", response);
    }

    @Override
    public void softDeleteResponse(String courseId, String teacherId, String responseId) {

        String transactionId = request.getHeader("X-Transaction-Id");
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("RESPOND")));

        kafkaTemplate.send("response-soft-delete-topic", responseId);
    }

    @Override
    public void hardDeleteResponse(String courseId, String teacherId, String responseId) {

        String transactionId = request.getHeader("X-Transaction-Id");
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("RESPOND")));

        kafkaTemplate.send("response-hard-delete-topic", responseId);
    }
}
