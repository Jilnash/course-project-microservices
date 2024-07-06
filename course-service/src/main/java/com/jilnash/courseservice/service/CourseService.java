package com.jilnash.courseservice.service;

import com.jilnash.courseservice.model.Course;

import java.util.List;

public interface CourseService {

    List<Course> getCourses(String name);

    Course getCourse(Long id);

    Course saveCourse(Course course);
}
