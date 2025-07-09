package com.jilnash.courseservice.controller.v1;

import com.jilnash.courseservice.dto.AppResponse;
import com.jilnash.courseservice.dto.course.CourseCreateDTO;
import com.jilnash.courseservice.service.course.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<?> getCourses(@RequestParam(required = false, defaultValue = "") String name) {

        log.info("[CONTROLLER] Fetching courses with name: {}", name);

        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Courses fetched successfully",
                        courseService.getCourses(name)
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@Validated @RequestBody CourseCreateDTO courseDTO,
                                          @RequestHeader("X-User-Sub") String teacherId) {

        log.info("[CONTROLLER] Creating course with name: {}", courseDTO.getName());

        courseDTO.setAuthorId(teacherId);

        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Course created successfully",
                        courseService.createCourse(courseDTO)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable String id) {

        log.info("[CONTROLLER] Fetching course with id: {}", id);

        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Course fetched successfully",
                        courseService.getCourse(id)
                )
        );
    }

    @GetMapping("/{id}/author")
    public ResponseEntity<?> getCourseAuthor(@PathVariable String id) {

        log.info("[CONTROLLER] Fetching course author for id: {}", id);

        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Course author fetched successfully",
                        courseService.getCourseAuthor(id)
                )
        );
    }

    @GetMapping("/{id}/name")
    public ResponseEntity<?> getCourseName(@PathVariable String id) {

        log.info("[CONTROLLER] Fetching course name for id: {}", id);

        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Course name fetched successfully",
                        courseService.getCourseName(id)
                )
        );
    }

    @GetMapping("/{id}/description")
    public ResponseEntity<?> getCourseDescription(@PathVariable String id) {

        log.info("[CONTROLLER] Fetching course description for id: {}", id);

        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Course description fetched successfully",
                        courseService.getCourseDescription(id)
                )
        );
    }

    @GetMapping("/{id}/duration")
    public ResponseEntity<?> getCourseDuration(@PathVariable String id) {

        log.info("[CONTROLLER] Fetching course duration for id: {}", id);

        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Course duration fetched successfully",
                        courseService.getCourseDuration(id)
                )
        );
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<?> updateCourseName(@PathVariable String id,
                                              @RequestBody String name) {

        log.info("[CONTROLLER] Updating course name for id: {}", id);

        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Course name updated successfully",
                        courseService.updateCourseName(id, name)
                )
        );
    }

    @PatchMapping("/{id}/description")
    public ResponseEntity<?> updateCourseDescription(@PathVariable String id,
                                                     @RequestBody String description) {

        log.info("[CONTROLLER] Updating course description for id: {}", id);

        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Course description updated successfully",
                        courseService.updateCourseDescription(id, description)
                )
        );
    }

    @PatchMapping("/{id}/duration")
    public ResponseEntity<?> updateCourseDuration(@PathVariable String id,
                                                  @RequestBody String duration) {

        log.info("[CONTROLLER] Updating course duration for id: {}", id);

        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Course duration updated successfully",
                        courseService.updateCourseDuration(id, duration)
                )
        );
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<?> softDeleteCourse(@PathVariable String id) {

        log.info("[CONTROLLER] Soft deleting course with id: {}", id);

        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Course soft deleted successfully",
                        courseService.softDelete(id)
                )
        );
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<?> hardDeleteCourse(@PathVariable String id) {

        log.info("[CONTROLLER] Hard deleting course with id: {}", id);

        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Course hard deleted successfully",
                        courseService.hardDelete(id)
                )
        );
    }
}
