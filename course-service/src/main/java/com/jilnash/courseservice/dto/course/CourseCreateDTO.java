package com.jilnash.courseservice.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseCreateDTO {

    private String id;

    private String authorId;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name should not be empty")
    private String name;

    @NotNull(message = "Description is required")
    @NotBlank(message = "Description should not be empty")
    private String description;

    @NotNull(message = "Duration is required")
    @NotBlank(message = "Duration should not be empty")
    private String duration;
}
