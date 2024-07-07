package com.jilnash.courseservice.controller.v1;

import com.jilnash.courseservice.dto.AppResponse;
import com.jilnash.courseservice.dto.course.CourseCreateDTO;
import com.jilnash.courseservice.dto.course.CourseUpdateDTO;
import com.jilnash.courseservice.mapper.CourseMapper;
import com.jilnash.courseservice.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseMapper courseMapper;

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
                        "Courses fetched successfully",
                        courseService.saveCourse(courseMapper.toEntity(courseDTO))
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable Long id) {
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
            @PathVariable Long id,
            @Validated @RequestBody CourseUpdateDTO courseDTO) {

        //checking if course exists
        //if so setting id, authorId
        courseDTO.setAuthorId(courseService.getCourse(id).getAuthor());
        courseDTO.setId(id);

        return ResponseEntity.ok(
                new AppResponse(
                        400,
                        "Course updated successfully",
                        courseService.saveCourse(courseMapper.toEntity(courseDTO))
                )
        );
    }
}
