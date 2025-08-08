package com.jilnash.courseaccessservice.listener;

import com.jilnash.courseaccessservice.service.StudentCourseAccessService;
import com.jilnash.courseaccessservicedto.dto.CheckAccessDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccessListener {

    private final StudentCourseAccessService studentCourseAccessService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public AccessListener(StudentCourseAccessService studentCourseAccessService, KafkaTemplate<String, String> kafkaTemplate) {
        this.studentCourseAccessService = studentCourseAccessService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "check-course-access-topic", groupId = "course-access-group")
    public void checkStudentAccess(CheckAccessDTO dto) {
        boolean hasAccess = studentCourseAccessService.getStudentHasAccess(dto.studentId(), dto.courseId());
        System.out.println(dto.transactionId());
        System.out.println(hasAccess);
        if (!hasAccess)
            kafkaTemplate.send("rollback-topic", dto.transactionId());
    }
}
