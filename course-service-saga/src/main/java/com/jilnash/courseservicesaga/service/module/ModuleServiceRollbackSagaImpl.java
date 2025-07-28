package com.jilnash.courseservicesaga.service.module;

import com.jilnash.courseservicedto.dto.module.ModuleDeleteDTO;
import com.jilnash.courseservicedto.dto.module.ModuleUpdateDescriptionDTO;
import com.jilnash.courseservicedto.dto.module.ModuleUpdateNameDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ModuleServiceRollbackSagaImpl implements ModuleServiceRollbackSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ModuleServiceRollbackSagaImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void createModuleRollback(String courseId) {
        kafkaTemplate.send("module-create-rollback-topic", courseId);
    }

    @Override
    public void updateModuleNameRollback(String courseId, String moduleId, String prevName) {
        kafkaTemplate.send("module-update-name-rollback-topic", new ModuleUpdateNameDTO(courseId, moduleId, prevName));
    }

    @Override
    public void updateModuleDescriptionRollback(String courseId, String moduleId, String prevDescription) {
        kafkaTemplate.send("module-update-description-rollback-topic", new ModuleUpdateDescriptionDTO(courseId, moduleId, prevDescription));
    }

    @Override
    public void softDeleteModuleRollback(String courseId, String moduleId) {
        kafkaTemplate.send("module-soft-delete-rollback-topic", new ModuleDeleteDTO(courseId, moduleId));
    }
}
