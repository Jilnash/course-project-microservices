package com.jilnash.hwservicesaga.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class HomeworkCreateSagaDTO {

    private String homeworkId;

    private String courseId;

    @NotNull(message = "Task ID cannot be null")
    @NotEmpty(message = "Task ID cannot be empty")
    private String taskId;

    private String studentId;

    @NotNull(message = "Files cannot be null")
    @NotEmpty(message = "Files cannot be empty")
    private List<MultipartFile> files;
}
