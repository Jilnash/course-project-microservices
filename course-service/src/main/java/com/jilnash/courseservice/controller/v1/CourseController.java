package com.jilnash.courseservice.controller.v1;

import com.jilnash.courseservice.dto.AppResponse;
import com.jilnash.courseservice.dto.course.CourseCreateDTO;
import com.jilnash.courseservice.dto.course.CourseUpdateDTO;
import com.jilnash.courseservice.service.course.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @GetMapping
    public ResponseEntity<?> getCourses(@RequestParam(required = false, defaultValue = "") String name) {
        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Courses fetched successfully",
                        courseService.getCourses(name)
                )
        );
    }

    @PutMapping
    public ResponseEntity<?> createCourse(@Validated @RequestBody CourseCreateDTO courseDTO) {
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
            @Validated @RequestBody CourseUpdateDTO courseDTO) {

        courseDTO.setId(id);

        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Course updated successfully",
                        courseService.update(courseDTO)
                )
        );
    }
}
