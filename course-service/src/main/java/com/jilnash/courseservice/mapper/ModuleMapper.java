package com.jilnash.courseservice.mapper;

import com.jilnash.courseservice.dto.module.ModuleCreateDTO;
import com.jilnash.courseservice.model.Module;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapper {

    public Module toNode(ModuleCreateDTO moduleCreateDTO) {
        return Module.builder()
                .name(moduleCreateDTO.getName())
                .description(moduleCreateDTO.getDescription())
                .course(moduleCreateDTO.getCourse())
                .build();
    }
}
