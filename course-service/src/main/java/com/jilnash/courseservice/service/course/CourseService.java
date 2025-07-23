package com.jilnash.courseservice.service.course;

import com.jilnash.courseservice.model.Course;
import com.jilnash.courseservicedto.dto.course.CourseCreateDTO;

import java.util.List;

public interface CourseService {

    List<Course> getCourses(String name);

    Course getCourse(String id);

    String getCourseAuthor(String courseId);

    String getCourseName(String courseId);

    String getCourseDescription(String courseId);

    String getCourseDuration(String courseId);

    Course createCourse(CourseCreateDTO course);

    Boolean updateCourseName(String courseId, String name);

    Boolean updateCourseDescription(String courseId, String description);

    Boolean updateCourseDuration(String courseId, String duration);

    Boolean softDelete(String id);

    Boolean hardDelete(String id);
}
