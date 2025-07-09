package com.jilnash.courseservice.mapper;

import com.jilnash.courseservice.dto.module.ModuleCreateDTO;
import com.jilnash.courseservice.dto.module.ModuleResponseDTO;
import com.jilnash.courseservice.model.Module;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Mapper class to map between Module entities and their corresponding DTOs.
 * This class is responsible for converting data between Module, ModuleCreateDTO, and ModuleResponseDTO objects.
 */
@Component
public class ModuleMapper {

    /**
     * Converts a ModuleCreateDTO object to a Module entity.
     *
     * @param moduleCreateDTO the ModuleCreateDTO object containing the details of the module to be converted
     * @return a Module entity constructed based on the provided ModuleCreateDTO
     */
    public Module toNode(ModuleCreateDTO moduleCreateDTO) {
        return Module.builder()
                .name(moduleCreateDTO.getName())
                .description(moduleCreateDTO.getDescription())
                .course(moduleCreateDTO.getCourse())
                .createdAt(new Date())
                .createdBy(moduleCreateDTO.getAuthorId())
                .build();
    }

    /**
     * Converts a Module entity into a ModuleResponseDTO.
     *
     * @param module the Module entity to be converted
     * @return the ModuleResponseDTO constructed based on the provided Module entity
     */
    public ModuleResponseDTO toResponse(Module module) {
        return ModuleResponseDTO.builder()
                .id(module.getId())
                .name(module.getName())
                .description(module.getDescription())
                .build();
    }
}
