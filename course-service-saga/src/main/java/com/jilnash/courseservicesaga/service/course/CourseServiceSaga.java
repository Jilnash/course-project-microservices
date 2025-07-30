package com.jilnash.courseservicesaga.service.course;

import com.jilnash.courseservicedto.dto.course.CourseCreateDTO;
import org.springframework.stereotype.Service;

@Service
interface CourseServiceSaga {

    void createCourse(CourseCreateDTO courseCreateDTO);

    void updateCourseName(String teacherId, String courseId, String name);

    void updateCourseDescription(String teacherId, String courseId, String description);

    void updateCourseDuration(String teacherId, String courseId, String duration);

    void softDeleteCourse(String teacherId, String courseId);

    void hardDeleteCourse(String teacherId, String courseId);
}
