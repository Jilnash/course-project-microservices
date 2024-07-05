package com.jilnash.teacherservice.controller.v1;

import com.jilnash.teacherservice.dto.AppResponse;
import com.jilnash.teacherservice.dto.TeacherCreateDTO;
import com.jilnash.teacherservice.dto.TeacherUpdateDTO;
import com.jilnash.teacherservice.mapper.TeacherMapper;
import com.jilnash.teacherservice.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherController {

    @Autowired
    public TeacherMapper teacherMapper;
    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public ResponseEntity<?> getTeachers() {

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Teachers found successfully",
                        teacherService.getTeachers()

                )
        );
    }

    @PutMapping
    public ResponseEntity<?> createTeacher(@Validated @RequestBody TeacherCreateDTO teacherDTO) {

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Teachers created successfully",
                        teacherService.saveTeacher(teacherMapper.toEntity(teacherDTO))
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacher(@PathVariable Long id) {

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Teacher found successfully",
                        teacherService.getTeacher(id)
                )
        );
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateTeacher(@PathVariable Long id,
                                           @Validated @RequestBody TeacherUpdateDTO teacherDTO) {

        // checking if teacher exists then setting the id and userId
        teacherDTO.setUserId(teacherService.getTeacher(id).getUserId());
        teacherDTO.setId(id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Teacher updated successfully",
                        teacherService.saveTeacher(teacherMapper.toEntity(teacherDTO))
                )
        );
    }
}
