package com.jilnash.courseservice.dto.module;

import com.jilnash.courseservice.model.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleCreateDTO {

    private String teacherId;

    @NotNull(message = "Module name is mandatory")
    @NotBlank(message = "Module name is mandatory")
    private String name;

    @NotNull(message = "Module description is mandatory")
    @NotBlank(message = "Module description is mandatory")
    private String description;

    private String courseId;

    private Course course;
}
