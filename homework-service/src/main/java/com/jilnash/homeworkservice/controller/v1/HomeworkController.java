package com.jilnash.homeworkservice.controller.v1;

import com.jilnash.homeworkservice.dto.AppResponse;
import com.jilnash.homeworkservice.dto.HomeworkCreateDTO;
import com.jilnash.homeworkservice.mapper.HomeworkMapper;
import com.jilnash.homeworkservice.service.HomeworkServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequestMapping(value = "/api/v1/homeworks")
@RequiredArgsConstructor
public class HomeworkController {

    @Autowired
    private final HomeworkServiceImpl homeworkService;

    @Autowired
    private final HomeworkMapper homeworkMapper;

    @GetMapping
    public ResponseEntity<?> getHomeworks(
            @RequestParam(required = false) Long taskId,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Boolean checked,
            @RequestParam(required = false) Date createdAfter
    ) {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Homeworks fetched successfully",
                        homeworkService.getHomeworks(taskId, studentId, checked, createdAfter)
                )
        );
    }

    @PutMapping
    public ResponseEntity<?> createHomework(@Validated @RequestBody HomeworkCreateDTO homeworkDTO) {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Homework updated successfully",
                        homeworkService.saveHomework(homeworkMapper.toEntity(homeworkDTO))
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHomework(@PathVariable Long id) {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Homework fetched successfully",
                        homeworkService.getHomework(id)
                )
        );
    }

    @GetMapping("{id}/task/id")
    public String getTaskId(@PathVariable Long id) {
        return homeworkService.getHwTaskId(id);
    }
}

