package com.jilnash.courseservicesaga.service.module;

import com.jilnash.courseservicesaga.dto.module.ModuleCreateDTO;
import org.springframework.stereotype.Service;

@Service
public interface ModuleServiceSaga {

    void createModule(ModuleCreateDTO module);

    void updateModuleName(String courseId, String id, String name);

    void updateModuleDescription(String courseId, String id, String description);

    void softDeleteModule(String courseId, String moduleId);

    void hardDeleteModule(String courseId, String moduleId);
}
