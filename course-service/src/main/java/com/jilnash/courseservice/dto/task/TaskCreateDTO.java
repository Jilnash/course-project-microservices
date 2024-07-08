package com.jilnash.courseservice.dto.task;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateDTO {

    @NotNull(message = "Module id is required")
    @Min(value = 1, message = "Module id must be greater than 0")
    private Long moduleId;

    @NotNull(message = "Author id is required")
    @Min(value = 1, message = "Author id must be greater than 0")
    private Long author;

    @NotNull(message = "Title is required")
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Description is required")
    @NotBlank(message = "Description is required")
    private String description;

    private String videoLink;

    @NotNull(message = "Audio required is required")
    private Boolean audioRequired;

    @NotNull(message = "Video required is required")
    private Boolean videoRequired;
}
