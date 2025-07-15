package com.jilnash.courseaccessservice.service;

import org.springframework.stereotype.Component;

@Component
public interface StudentCourseAccessServiceRollback {

    Boolean purchaseRollback(String studentId, String courseId);
}
