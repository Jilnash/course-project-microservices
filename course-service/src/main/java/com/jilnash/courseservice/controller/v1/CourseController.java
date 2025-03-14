package com.jilnash.courseservice.controller.v1;

import com.jilnash.courseservice.dto.AppResponse;
import com.jilnash.courseservice.dto.course.CourseCreateDTO;
import com.jilnash.courseservice.dto.course.CourseUpdateDTO;
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

    @PutMapping
    public ResponseEntity<?> createCourse(@Validated @RequestBody CourseCreateDTO courseDTO,
                                          @RequestHeader("X-User-Sub") String teacherId) {

        log.info("[CONTROLLER] Creating course with name: {}", courseDTO.getName());

        courseDTO.setAuthorId(teacherId);

        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Course created successfully",
                        courseService.create(courseDTO)
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

    @PostMapping("/{id}")
    public ResponseEntity<?> updateCourse(
            @PathVariable String id,
            @Validated @RequestBody CourseUpdateDTO courseDTO,
            @RequestHeader("X-User-Sub") String teacherId) {

        log.info("[CONTROLLER] Updating course with id: {}", id);

        courseDTO.setId(id);
        courseDTO.setTeacherId(teacherId);

        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Course updated successfully",
                        courseService.update(courseDTO)
                )
        );
    }
}
