package com.jilnash.courseservicesaga.service.course;

import org.springframework.stereotype.Service;

@Service
public interface CourseServiceRollbackSaga {

    void createCourseRollback(String courseId);

    void updateCourseNameRollback(String courseId, String prevName);

    void updateCourseDescriptionRollback(String courseId, String prevDescription);

    void updateCourseDurationRollback(String courseId, String prevDuration);

    void deleteCourseRollback(String courseId);
}
