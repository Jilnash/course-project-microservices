package com.jilnash.courseservice.mapper;

import com.jilnash.courseservice.dto.module.ModuleCreateDTO;
import com.jilnash.courseservice.dto.module.ModuleResponseDTO;
import com.jilnash.courseservice.model.Module;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapper {

    public static Module toNode(ModuleCreateDTO moduleCreateDTO) {
        return Module.builder()
                .name(moduleCreateDTO.getName())
                .description(moduleCreateDTO.getDescription())
                .course(moduleCreateDTO.getCourse())
                .build();
    }

    public static ModuleResponseDTO toResponse(Module module) {
        return ModuleResponseDTO.builder()
                .id(module.getId())
                .name(module.getName())
                .description(module.getDescription())
                .build();
    }
}
