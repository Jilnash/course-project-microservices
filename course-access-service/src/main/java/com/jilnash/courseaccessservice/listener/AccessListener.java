package com.jilnash.courseaccessservice.listener;

import com.jilnash.courseaccessservice.service.StudentCourseAccessService;
import com.jilnash.courseaccessservicedto.dto.CheckAccessDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AccessListener {

    private final StudentCourseAccessService studentCourseAccessService;

    public AccessListener(StudentCourseAccessService studentCourseAccessService) {
        this.studentCourseAccessService = studentCourseAccessService;
    }

    @KafkaListener(topics = "check-course-access-topic", groupId = "course-access-group")
    public void checkStudentAccess(CheckAccessDTO dto) {
        boolean hasAccess = studentCourseAccessService.getStudentHasAccess(dto.studentId(), dto.courseId());
        if (hasAccess) {
            // Logic for granting access
            System.out.println("Access granted for student: " + dto.studentId() + " to course: " + dto.courseId());
        } else {
            // Logic for denying access
            System.out.println("Access denied for student: " + dto.studentId() + " to course: " + dto.courseId());
        }
    }
}
