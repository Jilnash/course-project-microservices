package com.jilnash.courseservicesaga.service.module;

import com.jilnash.courserightsservicedto.dto.CheckRightsDTO;
import com.jilnash.courseservicedto.dto.module.ModuleCreateDTO;
import com.jilnash.courseservicedto.dto.module.ModuleUpdateDescriptionDTO;
import com.jilnash.courseservicedto.dto.module.ModuleUpdateNameDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ModuleServiceSagaImpl implements ModuleServiceSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final HttpServletRequest request;

    public ModuleServiceSagaImpl(KafkaTemplate<String, Object> kafkaTemplate,
                                 HttpServletRequest request) {
        this.kafkaTemplate = kafkaTemplate;
        this.request = request;
    }

    @Override
    public void createModule(ModuleCreateDTO module) {

        String transactionId = request.getHeader("X-Transaction-Id");

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, module.getCourseId(), module.getAuthorId(), Set.of("CREATE")));
        kafkaTemplate.send("module-create-topic", module);

    }

    @Override
    public void updateModuleName(String teacherId, String courseId, String id, String name) {

        String transactionId = request.getHeader("X-Transaction-Id");

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("UPDATE")));

        kafkaTemplate.send("module-update-name-topic", new ModuleUpdateNameDTO(courseId, id, name));

    }

    @Override
    public void updateModuleDescription(String teacherId, String courseId, String id, String description) {

        String transactionId = request.getHeader("X-Transaction-Id");

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("UPDATE")));

        kafkaTemplate.send("module-update-description-topic", new ModuleUpdateDescriptionDTO(courseId, id, description));

    }

    @Override
    public void softDeleteModule(String teacherId, String courseId, String id) {

        String transactionId = request.getHeader("X-Transaction-Id");

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("DELETE")));

        kafkaTemplate.send("module-soft-delete-topic", id);

    }

    @Override
    public void hardDeleteModule(String teacherId, String courseId, String id) {

        String transactionId = request.getHeader("X-Transaction-Id");

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("DELETE")));

        kafkaTemplate.send("module-hard-delete-topic", id);

    }
}
