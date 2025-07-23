package com.jilnash.courseservicesaga.service.module;

import com.jilnash.courseservicedto.dto.module.ModuleCreateDTO;
import com.jilnash.courseservicedto.dto.module.ModuleUpdateDescriptionDTO;
import com.jilnash.courseservicedto.dto.module.ModuleUpdateNameDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ModuleServiceSagaImpl implements ModuleServiceSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ModuleServiceSagaImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void createModule(ModuleCreateDTO module) {

        String id = UUID.randomUUID().toString();
        module.setId(id);

        try {
            //todo: check teacher permissions
            kafkaTemplate.send("module-create-topic", module);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateModuleName(String courseId, String id, String name) {
        try {
            //todo: check teacher permissions
            kafkaTemplate.send("module-update-name-topic", new ModuleUpdateNameDTO(courseId, id, name));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateModuleDescription(String courseId, String id, String description) {
        try {
            //todo: check teacher permissions
            kafkaTemplate.send("module-update-description-topic", new ModuleUpdateDescriptionDTO(courseId, id, description));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void softDeleteModule(String courseId, String id) {
        try {
            //todo: check teacher permissions
            kafkaTemplate.send("module-soft-delete-topic", id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void hardDeleteModule(String courseId, String id) {
        try {
            //todo: check teacher permissions
            kafkaTemplate.send("module-hard-delete-topic", id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
