package com.jilnash.homeworkservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkCreateDTO {

    private String studentId;

    @NotNull(message = "Task id is required")
    private String taskId;

    private Boolean checked = false;

    private MultipartFile image;

    private MultipartFile audio;

    private MultipartFile video;
}
