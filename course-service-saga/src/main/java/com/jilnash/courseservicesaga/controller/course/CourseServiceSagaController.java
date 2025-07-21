package com.jilnash.courseservicesaga.controller.course;

import com.jilnash.courseservicesaga.dto.course.CourseCreateDTO;
import com.jilnash.courseservicesaga.service.course.CourseServiceSagaImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseServiceSagaController {

    private final CourseServiceSagaImpl courseServiceSaga;

    public CourseServiceSagaController(CourseServiceSagaImpl courseServiceSaga) {
        this.courseServiceSaga = courseServiceSaga;
    }

    @PostMapping
    public ResponseEntity<?> createCourse(
            @RequestHeader(name = "X-User-Sub", required = false) String authorId,
            @RequestBody @Validated CourseCreateDTO courseCreateDTO) {

        courseCreateDTO.setAuthorId(authorId);
        courseServiceSaga.createCourse(courseCreateDTO);

        return ResponseEntity.ok("Course created successfully");
    }

    @PatchMapping("/{courseId}/name")
    public ResponseEntity<?> updateCourseName(@PathVariable String courseId,
                                              @RequestBody String newName) {

        courseServiceSaga.updateCourseName(courseId, newName);

        return ResponseEntity.ok("Course name updated successfully");
    }

    @PatchMapping("/{courseId}/description")
    public ResponseEntity<?> updateCourseDescription(@PathVariable String courseId,
                                                     @RequestBody String newDescription) {

        courseServiceSaga.updateCourseDescription(courseId, newDescription);

        return ResponseEntity.ok("Course description updated successfully");
    }

    @PatchMapping("/{courseId}/duration")
    public ResponseEntity<?> updateCourseDuration(@PathVariable String courseId,
                                                  @RequestBody String newDuration) {

        courseServiceSaga.updateCourseDuration(courseId, newDuration);

        return ResponseEntity.ok("Course duration updated successfully");
    }

    @DeleteMapping("/{courseId}/soft")
    public ResponseEntity<?> deleteCourse(@PathVariable String courseId) {

        courseServiceSaga.softDeleteCourse(courseId);

        return ResponseEntity.ok("Course with ID " + courseId + " deleted successfully");
    }

    @DeleteMapping("/{courseId}/hard")
    public ResponseEntity<?> hardDeleteCourse(@PathVariable String courseId) {

        courseServiceSaga.hardDeleteCourse(courseId);

        return ResponseEntity.ok("Course with ID " + courseId + " hard deleted successfully");
    }
}
