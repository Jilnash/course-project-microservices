package com.jilnash.courseservice.listener;

import com.jilnash.courseservice.service.module.ModuleServiceRollback;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ModuleRollbackListener {

    private final ModuleServiceRollback moduleServiceRollback;

    public ModuleRollbackListener(ModuleServiceRollback moduleServiceRollback) {
        this.moduleServiceRollback = moduleServiceRollback;
    }

    @KafkaListener(topics = "module-create-rollback-topic", groupId = "course-service-group")
    public void handleModuleCreateRollback(String moduleId) {
        moduleServiceRollback.createModuleRollback(moduleId);
    }

    @KafkaListener(topics = "module-update-name-rollback-topic", groupId = "course-service-group")
    public void handleModuleUpdateNameRollback(String moduleId) {
        moduleServiceRollback.updateModuleNameRollback(moduleId);
    }

    @KafkaListener(topics = "module-update-description-rollback-topic", groupId = "course-service-group")
    public void handleModuleUpdateDescriptionRollback(String moduleId) {
        moduleServiceRollback.updateModuleDescriptionRollback(moduleId);
    }

    @KafkaListener(topics = "module-soft-delete-rollback-topic", groupId = "course-service-group")
    public void handleModuleSoftDeleteRollback(String moduleId) {
        moduleServiceRollback.softDeleteRollback(moduleId);
    }
}
