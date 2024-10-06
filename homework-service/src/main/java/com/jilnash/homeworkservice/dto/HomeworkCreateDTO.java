package com.jilnash.homeworkservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkCreateDTO {

    private String studentId;

    @NotNull(message = "Task id is required")
    @Min(value = 1, message = "Task id must be greater than 0")
    private String taskId;

    private String audioLink;

    private String videoLink;

    private Boolean checked = false;
}
