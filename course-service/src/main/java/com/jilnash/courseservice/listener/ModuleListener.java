package com.jilnash.courseservice.listener;

import com.jilnash.courseservice.dto.module.ModuleCreateDTO;
import com.jilnash.courseservice.dto.module.ModuleUpdateDescriptionDTO;
import com.jilnash.courseservice.dto.module.ModuleUpdateNameDTO;
import com.jilnash.courseservice.service.module.ModuleService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ModuleListener {

    private final ModuleService moduleService;

    public ModuleListener(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @KafkaListener(topics = "module-create-topic", groupId = "course-service-group")
    public void createModuleListener(ModuleCreateDTO moduleCreateDTO) {
        try {
            moduleService.createModule(moduleCreateDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error creating module with ID: " + moduleCreateDTO.getId(), e);
        }
    }

    @KafkaListener(topics = "module-update-name-topic", groupId = "course-service-group")
    public void updateModuleNameListener(ModuleUpdateNameDTO dto) {
        System.out.println(dto);
        try {
            moduleService.updateModuleName(dto.courseId(), dto.id(), dto.name());
        } catch (Exception e) {
            throw new RuntimeException("Error updating module name for ID: " + dto.id(), e);
        }
    }

    @KafkaListener(topics = "module-update-description-topic", groupId = "course-service-group")
    public void updateModuleDescriptionListener(ModuleUpdateDescriptionDTO dto) {
        try {
            moduleService.updateModuleDescription(dto.courseId(), dto.id(), dto.description());
        } catch (Exception e) {
            throw new RuntimeException("Error updating module description for ID: " + dto.id(), e);
        }
    }

    @KafkaListener(topics = "module-soft-delete-topic", groupId = "course-service-group")
    public void softDeleteModuleListener(String moduleId) {
        try {
            moduleService.softDeleteModule(moduleId);
        } catch (Exception e) {
            throw new RuntimeException("Error soft deleting module with ID: " + moduleId, e);
        }
    }

    @KafkaListener(topics = "module-hard-delete-topic", groupId = "course-service-group")
    public void hardDeleteModuleListener(String moduleId) {
        try {
            moduleService.hardDeleteModule(moduleId);
        } catch (Exception e) {
            throw new RuntimeException("Error hard deleting module with ID: " + moduleId, e);
        }
    }
}
