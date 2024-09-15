package com.jilnash.courseservice.service.course;

import com.jilnash.courseservice.dto.course.CourseCreateDTO;
import com.jilnash.courseservice.dto.course.CourseUpdateDTO;
import com.jilnash.courseservice.model.Course;

import java.util.List;

public interface CourseService {

    List<Course> getCourses(String name);

    Course getCourse(String id);

    Course create(CourseCreateDTO course);

    Course update(CourseUpdateDTO course);

    Course delete(String id);
}
