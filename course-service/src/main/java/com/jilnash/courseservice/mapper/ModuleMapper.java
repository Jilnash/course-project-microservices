package com.jilnash.courseservice.mapper;

import com.jilnash.courseservice.dto.module.ModuleCreateDTO;
import com.jilnash.courseservice.dto.module.ModuleUpdateDTO;
import com.jilnash.courseservice.model.Course;
import com.jilnash.courseservice.model.Module;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapper {

    public Module toEntity(ModuleCreateDTO moduleCreateDTO) {

        return Module.builder()
                .name(moduleCreateDTO.getName())
                .description(moduleCreateDTO.getDescription())
                .course(Course.builder().id(moduleCreateDTO.getCourseId()).build())
                .build();
    }

    public Module toEntity(ModuleUpdateDTO moduleUpdateDTO) {

        return Module.builder()
                .id(moduleUpdateDTO.getId())
                .name(moduleUpdateDTO.getName())
                .description(moduleUpdateDTO.getDescription())
                .build();
    }
}
