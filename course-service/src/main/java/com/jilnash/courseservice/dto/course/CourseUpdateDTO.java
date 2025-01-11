package com.jilnash.courseservice.dto.course;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseUpdateDTO {

    private String id;

    private String teacherId;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name should not be empty")
    private String name;

    @NotNull(message = "Description is required")
    @NotBlank(message = "Description should not be empty")
    private String description;

    @NotNull(message = "Duration is required")
    @NotBlank(message = "Duration should not be empty")
    private String duration;

    @NotNull(message = "Homework posting day interval is required")
    @Min(value = 0, message = "Homework posting day interval must be not negative")
    private Integer hwPostingDayInterval;
}
