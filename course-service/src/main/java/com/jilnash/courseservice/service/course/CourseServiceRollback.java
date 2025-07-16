package com.jilnash.courseservice.service.course;

import org.springframework.stereotype.Component;

@Component
public interface CourseServiceRollback {

    Boolean createCourseRollback(String createdCourse);

    Boolean updateCourseNameRollback(String courseId, String prevName);

    Boolean updateCourseDescriptionRollback(String courseId, String prevDescription);

    Boolean updateCourseDurationRollback(String courseId, String prevDuration);

    Boolean softDeleteRollback(String id);
}
