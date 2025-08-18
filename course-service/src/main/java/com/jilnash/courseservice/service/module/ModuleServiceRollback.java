package com.jilnash.courseservice.service.module;

import org.springframework.stereotype.Component;

@Component
public interface ModuleServiceRollback {

    Boolean createModuleRollback(String createdModule);

    Boolean updateModuleNameRollback(String moduleId);

    Boolean updateModuleDescriptionRollback(String moduleId);

    Boolean softDeleteRollback(String id);
}
