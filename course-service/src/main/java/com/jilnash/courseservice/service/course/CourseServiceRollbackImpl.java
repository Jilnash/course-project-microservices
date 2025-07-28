package com.jilnash.courseservice.service.course;

import com.jilnash.courseservice.repo.CourseRepo;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class CourseServiceRollbackImpl implements CourseServiceRollback {

    private final CourseRepo courseRepo;

    public CourseServiceRollbackImpl(CourseRepo courseRepo) {
        this.courseRepo = courseRepo;
    }

    @Override
    public Boolean createCourseRollback(String createdCourse) {
        return courseRepo.findById(createdCourse)
                .map(course -> {
                    courseRepo.deleteById(createdCourse);
                    return true;
                })
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + createdCourse));
    }

    @Override
    public Boolean updateCourseNameRollback(String courseId, String prevName) {
        courseRepo.updateCourseName(courseId, prevName);
        return true;
    }

    @Override
    public Boolean updateCourseDescriptionRollback(String courseId, String prevDescription) {
        courseRepo.updateCourseDescription(courseId, prevDescription);
        return true;
    }

    @Override
    public Boolean updateCourseDurationRollback(String courseId, String prevDuration) {
        courseRepo.updateCourseDuration(courseId, prevDuration);
        return true;
    }

    @Override
    public Boolean softDeleteRollback(String id) {
        return courseRepo.findById(id).
                map(course -> {
                    course.setDeletedAt(null);
                    courseRepo.save(course);
                    return true;
                })
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + id));
    }
}
