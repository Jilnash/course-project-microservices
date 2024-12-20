package com.jilnash.homeworkservice.dto;

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
    private String taskId;

    private String audioLink;

    private String videoLink;

    private Boolean checked = false;
}
