package com.jilnash.courseservicesaga.service.module;

import com.jilnash.courseservicedto.dto.module.ModuleCreateDTO;
import org.springframework.stereotype.Service;

@Service
public interface ModuleServiceSaga {

    void createModule(ModuleCreateDTO module);

    void updateModuleName(String teacherId, String courseId, String id, String name);

    void updateModuleDescription(String teacherId, String courseId, String id, String description);

    void softDeleteModule(String teacherId, String courseId, String moduleId);

    void hardDeleteModule(String teacherId, String courseId, String moduleId);
}
