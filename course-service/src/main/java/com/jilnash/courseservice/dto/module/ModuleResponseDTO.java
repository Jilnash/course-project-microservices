package com.jilnash.courseservice.dto.module;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModuleResponseDTO {
    private String id;
    private String name;
    private String description;
}
