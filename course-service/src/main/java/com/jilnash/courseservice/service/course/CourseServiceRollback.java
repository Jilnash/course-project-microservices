package com.jilnash.courseservice.service.course;

import org.springframework.stereotype.Component;

@Component
public interface CourseServiceRollback {

    Boolean createCourseRollback(String createdCourse);

    Boolean updateCourseNameRollback(String courseId);

    Boolean updateCourseDescriptionRollback(String courseId);

    Boolean updateCourseDurationRollback(String courseId);

    Boolean softDeleteRollback(String id);
}
