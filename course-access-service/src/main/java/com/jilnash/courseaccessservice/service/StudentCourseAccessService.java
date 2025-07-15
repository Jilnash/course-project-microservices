package com.jilnash.courseaccessservice.service;

import com.jilnash.courseaccessservice.model.StudentCourseAccess;
import org.springframework.stereotype.Component;

@Component
public interface StudentCourseAccessService {

    Boolean getStudentHasAccess(String studentId, String courseId);

    StudentCourseAccess purchase(StudentCourseAccess studentCourseAccess);
}
