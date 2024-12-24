package com.jilnash.homeworkservice.controller.v1;

import com.jilnash.homeworkservice.dto.AppResponse;
import com.jilnash.homeworkservice.dto.HomeworkCreateDTO;
import com.jilnash.homeworkservice.mapper.HomeworkMapper;
import com.jilnash.homeworkservice.service.HomeworkServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

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
            @RequestPart @Validated HomeworkCreateDTO homeworkDTO,
            @RequestPart(required = false) MultipartFile image,
            @RequestPart(required = false) MultipartFile audio,
            @RequestPart(required = false) MultipartFile video,
            @RequestHeader("X-User-Sub") String studentId) {

        homeworkDTO.setStudentId(studentId);
        homeworkDTO.setImage(image);
        homeworkDTO.setAudio(audio);
        homeworkDTO.setVideo(video);

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
    public String getTaskId(@PathVariable Long id) {
        return homeworkService.getHwTaskId(id);
    }

    @GetMapping("{hwId}/student/id")
    public String getStudentId(@PathVariable Long hwId) {
        return homeworkService.getHwStudentId(hwId);
    }
}

