package com.jilnash.courseservice.dto.module;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleCreateDTO {

    @NotNull(message = "Module name is mandatory")
    @NotBlank(message = "Module name is mandatory")
    private String name;

    @NotNull(message = "Module description is mandatory")
    @NotBlank(message = "Module description is mandatory")
    private String description;

    @NotNull(message = "Course id is mandatory")
    @Min(value = 1, message = "Course id must be greater than 0")
    private Long courseId;
}
