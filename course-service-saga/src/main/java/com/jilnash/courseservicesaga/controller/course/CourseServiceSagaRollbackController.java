package com.jilnash.courseservicesaga.controller.course;

import com.jilnash.courseservicesaga.service.course.CourseServiceRollbackSaga;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseServiceSagaRollbackController {

    private final CourseServiceRollbackSaga courseServiceRollbackSaga;

    public CourseServiceSagaRollbackController(CourseServiceRollbackSaga courseServiceRollbackSaga) {
        this.courseServiceRollbackSaga = courseServiceRollbackSaga;
    }

    @PostMapping("{courseId}/rollback")
    public ResponseEntity<?> createCourse(@PathVariable String courseId) {

        courseServiceRollbackSaga.createCourseRollback(courseId);

        return ResponseEntity.ok("Course created successfully");
    }

    @PatchMapping("/{courseId}/name/rollback")
    public ResponseEntity<?> updateCourseName(@PathVariable String courseId,
                                              @RequestBody String prevName) {

        courseServiceRollbackSaga.updateCourseNameRollback(courseId, prevName);

        return ResponseEntity.ok("Course name updated successfully");
    }

    @PatchMapping("/{courseId}/description/rollback")
    public ResponseEntity<?> updateCourseDescription(@PathVariable String courseId,
                                                     @RequestBody String prevDescription) {

        courseServiceRollbackSaga.updateCourseDescriptionRollback(courseId, prevDescription);

        return ResponseEntity.ok("Course description updated successfully");
    }

    @PatchMapping("/{courseId}/duration/rollback")
    public ResponseEntity<?> updateCourseDuration(@PathVariable String courseId,
                                                  @RequestBody String prevDuration) {

        courseServiceRollbackSaga.updateCourseDurationRollback(courseId, prevDuration);

        return ResponseEntity.ok("Course price updated successfully");
    }

    @DeleteMapping("/{courseId}/soft/rollback")
    public ResponseEntity<?> deleteCourse(@PathVariable String courseId) {

        courseServiceRollbackSaga.deleteCourseRollback(courseId);

        return ResponseEntity.ok("Course with ID " + courseId + " deleted successfully");
    }
}
