package com.jilnash.courseservicesaga.controller.course;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseServiceSagaRollbackController {

    @PostMapping("{courseId}/rollback")
    public ResponseEntity<?> createCourse(@PathVariable String courseId) {

        return ResponseEntity.ok("Course created successfully");
    }

    @PutMapping("/{courseId}/name/rollback")
    public ResponseEntity<?> updateCourseName(@PathVariable String courseId) {

        return ResponseEntity.ok("Course name updated successfully");
    }

    @PutMapping("/{courseId}/description/rollback")
    public ResponseEntity<?> updateCourseDescription(@PathVariable String courseId) {

        return ResponseEntity.ok("Course description updated successfully");
    }

    @PutMapping("/{courseId}/price/rollback")
    public ResponseEntity<?> updateCoursePrice(@PathVariable String courseId) {

        return ResponseEntity.ok("Course price updated successfully");
    }

    @DeleteMapping("/{courseId}/soft/rollback")
    public ResponseEntity<?> deleteCourse(@PathVariable String courseId) {

        return ResponseEntity.ok("Course with ID " + courseId + " deleted successfully");
    }
}
