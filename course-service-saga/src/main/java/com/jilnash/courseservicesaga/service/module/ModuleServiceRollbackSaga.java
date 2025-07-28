package com.jilnash.courseservicesaga.service.module;

import org.springframework.stereotype.Service;

@Service
public interface ModuleServiceRollbackSaga {

    void createModuleRollback(String courseId);

    void updateModuleNameRollback(String courseId, String moduleId, String prevName);

    void updateModuleDescriptionRollback(String courseId, String moduleId, String prevDescription);

    void softDeleteModuleRollback(String courseId, String moduleId);
}
