package com.jilnash.homeworkservice.controller.v1;

import com.jilnash.homeworkservice.dto.AppResponse;
import com.jilnash.homeworkservice.dto.HomeworkCreateDTO;
import com.jilnash.homeworkservice.mapper.HomeworkMapper;
import com.jilnash.homeworkservice.service.HomeworkServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/homeworks")
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkServiceImpl homeworkService;

    private final HomeworkMapper homeworkMapper;

    @GetMapping
    public ResponseEntity<?> getHomeworks(
            @RequestParam(required = false) String taskId,
            @RequestParam(required = false) String studentId,
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
    public ResponseEntity<?> createHomework(
            @ModelAttribute @Validated HomeworkCreateDTO homeworkDTO,
            @RequestHeader("X-User-Sub") String studentId) {

        homeworkDTO.setStudentId(studentId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Homework created successfully",
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
    public String getTaskId(@PathVariable UUID id) {
        return homeworkService.getHwTaskId(id);
    }

    @GetMapping("{hwId}/student/id")
    public String getStudentId(@PathVariable UUID hwId) {
        return homeworkService.getHwStudentId(hwId);
    }
}

