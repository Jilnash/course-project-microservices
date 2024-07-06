package com.jilnash.courseservice.service;

import com.jilnash.courseservice.model.Course;
import com.jilnash.courseservice.repo.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepo courseRepo;

    @Override
    public List<Course> getCourses(String name) {
        return courseRepo.findAll();
    }

    @Override
    public Course getCourse(Long id) {
        return courseRepo
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + id));
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepo.save(course);
    }
}
