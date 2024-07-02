package com.jilnash.studentservice.controller.v1;

import com.jilnash.studentservice.dto.AppResponse;
import com.jilnash.studentservice.dto.StudentDTO;
import com.jilnash.studentservice.mapper.StudentMapper;
import com.jilnash.studentservice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentMapper studentMapper;

    @GetMapping
    public ResponseEntity<?> getStudents(@RequestParam(required = false) String login) {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Students found successfully",
                        studentService.getStudents()
                )
        );
    }

    @PutMapping
    public ResponseEntity<?> createStudent(@Validated @RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Student created successfully",
                        studentService.saveStudent(studentMapper.toEntity(studentDTO))
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Student found successfully",
                        studentService.getStudent(id)
                )
        );
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateStudent(@Validated @RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Student updated successfully",
                        studentService.saveStudent(studentMapper.toEntity(studentDTO))
                )
        );
    }
}
