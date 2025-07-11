package com.jilnash.homeworkservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkCreateDTO {

    @NotNull(message = "Student id is required")
    @NotEmpty(message = "Student id cannot be empty")
    private String studentId;

    @NotNull(message = "Task id is required")
    @NotEmpty(message = "Task id cannot be empty")
    private String taskId;

    private Boolean checked = false;

    @NotNull(message = "Files are required")
    @Size(min = 1, message = "At least one file is required")
    private List<MultipartFile> files;
}
