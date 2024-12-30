package com.jilnash.courseservice.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateDTO {

    private String id;

    private String courseId;

    private String moduleId;

    @NotNull(message = "Task title is required")
    @NotBlank(message = "Task title is required")
    private String title;

    @NotNull(message = "Task description is required")
    @NotBlank(message = "Task description is required")
    private String description;

    @URL(message = "Invalid video link")
    private String videoLink;
}
