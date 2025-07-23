package com.jilnash.courseservicesaga.service.course;

import com.jilnash.courseservicedto.dto.course.CourseCreateDTO;
import org.springframework.stereotype.Service;

@Service
interface CourseServiceSaga {

    void createCourse(CourseCreateDTO courseCreateDTO);

    void updateCourseName(String courseId, String name);

    void updateCourseDescription(String courseId, String description);

    void updateCourseDuration(String courseId, String duration);

    void softDeleteCourse(String courseId);

    void hardDeleteCourse(String courseId);
}
