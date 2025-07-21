package com.jilnash.courseservice.controller.v1;

import com.jilnash.courseservice.dto.AppResponse;
import com.jilnash.courseservice.service.course.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
}
