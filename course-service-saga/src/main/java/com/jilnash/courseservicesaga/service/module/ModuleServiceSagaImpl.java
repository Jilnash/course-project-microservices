package com.jilnash.courseservicesaga.service.module;

import com.jilnash.courserightsservicedto.dto.CheckRightsDTO;
import com.jilnash.courseservicedto.dto.module.ModuleCreateDTO;
import com.jilnash.courseservicedto.dto.module.ModuleUpdateDescriptionDTO;
import com.jilnash.courseservicedto.dto.module.ModuleUpdateNameDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class ModuleServiceSagaImpl implements ModuleServiceSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final Set<String> transactionIds;

    public ModuleServiceSagaImpl(KafkaTemplate<String, Object> kafkaTemplate,
                                 Set<String> transactionIds) {
        this.kafkaTemplate = kafkaTemplate;
        this.transactionIds = transactionIds;
    }

    @Override
    public void createModule(ModuleCreateDTO module) {

        String id = UUID.randomUUID().toString();
        module.setId(id);

        String transactionId = UUID.randomUUID().toString();
        transactionIds.add(transactionId);

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, module.getCourseId(), module.getAuthorId(), Set.of()));
        kafkaTemplate.send("module-create-topic", module);

    }

    @Override
    public void updateModuleName(String teacherId, String courseId, String id, String name) {

        String transactionId = UUID.randomUUID().toString();
        transactionIds.add(transactionId);

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("UPDATE")));

        kafkaTemplate.send("module-update-name-topic", new ModuleUpdateNameDTO(courseId, id, name));

    }

    @Override
    public void updateModuleDescription(String teacherId, String courseId, String id, String description) {

        String transactionId = UUID.randomUUID().toString();
        transactionIds.add(transactionId);

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of()));

        kafkaTemplate.send("module-update-description-topic", new ModuleUpdateDescriptionDTO(courseId, id, description));

    }

    @Override
    public void softDeleteModule(String teacherId, String courseId, String id) {

        String transactionId = UUID.randomUUID().toString();
        transactionIds.add(transactionId);

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of()));

        kafkaTemplate.send("module-soft-delete-topic", id);

    }

    @Override
    public void hardDeleteModule(String teacherId, String courseId, String id) {

        String transactionId = UUID.randomUUID().toString();
        transactionIds.add(transactionId);

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of()));

        kafkaTemplate.send("module-hard-delete-topic", id);

    }
}
