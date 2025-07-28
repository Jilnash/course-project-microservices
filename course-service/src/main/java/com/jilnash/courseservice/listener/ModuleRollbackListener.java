package com.jilnash.courseservice.listener;

import com.jilnash.courseservice.service.module.ModuleServiceRollback;
import com.jilnash.courseservicedto.dto.module.ModuleDeleteDTO;
import com.jilnash.courseservicedto.dto.module.ModuleUpdateDescriptionDTO;
import com.jilnash.courseservicedto.dto.module.ModuleUpdateNameDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ModuleRollbackListener {

    private final ModuleServiceRollback moduleServiceRollback;

    public ModuleRollbackListener(ModuleServiceRollback moduleServiceRollback) {
        this.moduleServiceRollback = moduleServiceRollback;
    }

    @KafkaListener(topics = "module-create-rollback-topic", groupId = "course-service-group")
    public void handleModuleCreateRollback(String courseId) {
        moduleServiceRollback.createModuleRollback(courseId);
    }

    @KafkaListener(topics = "module-update-name-rollback-topic", groupId = "course-service-group")
    public void handleModuleUpdateNameRollback(ModuleUpdateNameDTO dto) {
        moduleServiceRollback.updateModuleNameRollback(dto.id(), dto.name());
    }

    @KafkaListener(topics = "module-update-description-rollback-topic", groupId = "course-service-group")
    public void handleModuleUpdateDescriptionRollback(ModuleUpdateDescriptionDTO dto) {
        moduleServiceRollback.updateModuleDescriptionRollback(dto.id(), dto.description());
    }

    @KafkaListener(topics = "module-soft-delete-rollback-topic", groupId = "course-service-group")
    public void handleModuleSoftDeleteRollback(ModuleDeleteDTO dto) {
        moduleServiceRollback.softDeleteRollback(dto.id());
    }
}
