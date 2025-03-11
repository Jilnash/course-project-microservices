package com.jilnash.homeworkservice.controller.v1;

import com.jilnash.homeworkservice.dto.AppResponse;
import com.jilnash.homeworkservice.dto.HomeworkCreateDTO;
import com.jilnash.homeworkservice.mapper.HomeworkMapper;
import com.jilnash.homeworkservice.service.HomeworkServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.UUID;

@Slf4j
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

        log.info("[CONTROLLER] Fetching homeworks");
        log.debug("[CONTROLLER] Fetching homeworks with taskId: {}, studentId: {}, checked: {}, createdAfter: {}",
                taskId, studentId, checked, createdAfter);

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

        log.info("[CONTROLLER] Creating homework");
        log.debug("[CONTROLLER] Creating homework to taskId {} by studentId: {}", homeworkDTO.getTaskId(), studentId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Homework created successfully",
                        homeworkService.saveHomework(homeworkMapper.toEntity(homeworkDTO))
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHomework(@PathVariable UUID id) {

        log.info("[CONTROLLER] Fetching homework");
        log.debug("[CONTROLLER] Fetching homework with id: {}", id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Homework fetched successfully",
                        homeworkService.getHomeworkDTO(id)
                )
        );
    }

    @GetMapping("{id}/files/{fileName}")
    public ResponseEntity<?> getFile(@PathVariable UUID id, @PathVariable String fileName) {

        log.info("[CONTROLLER] Fetching homework file");
        log.debug("[CONTROLLER] Fetching homework file with id: {}, fileName: {}", id, fileName);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "File fetched successfully",
                        homeworkService.getFileURL(id, fileName)
                )
        );
    }

    @GetMapping("{id}/task/id")
    public String getTaskId(@PathVariable UUID id) {

        log.info("[CONTROLLER] Fetching task id");
        log.debug("[CONTROLLER] Fetching task id with homework id: {}", id);

        return homeworkService.getHwTaskId(id);
    }

    @GetMapping("{hwId}/student/id")
    public String getStudentId(@PathVariable UUID hwId) {

        log.info("[CONTROLLER] Fetching student id");
        log.debug("[CONTROLLER] Fetching student id with homework id: {}", hwId);

        return homeworkService.getHwStudentId(hwId);
    }

    @PutMapping("{hwId}/checked")
    public ResponseEntity<?> setChecked(@PathVariable UUID hwId) {

        log.info("[CONTROLLER] Checking homework");
        log.debug("[CONTROLLER] Checking homework with id: {}", hwId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Homework checked successfully",
                        homeworkService.setChecked(hwId)
                )
        );
    }
}

