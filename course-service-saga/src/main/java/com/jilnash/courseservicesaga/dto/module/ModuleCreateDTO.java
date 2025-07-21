package com.jilnash.courseservicesaga.dto.module;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleCreateDTO {

    private String id;

    private String courseId;

    private String authorId;

    @NotNull(message = "Module name is mandatory")
    @NotBlank(message = "Module name is mandatory")
    private String name;

    @NotNull(message = "Module description is mandatory")
    @NotBlank(message = "Module description is mandatory")
    private String description;
}
