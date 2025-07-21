package com.jilnash.courseservice.dto.module;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleCreateDTO {

    private String id;

    private String courseId;

    private String authorId;

    private String name;

    private String description;
}
