package com.jilnash.courseservice.service.module;

import org.springframework.stereotype.Component;

@Component
public interface ModuleServiceRollback {

    Boolean createModuleRollback(String createdModule);

    Boolean updateModuleNameRollback(String moduleId, String prevName);

    Boolean updateModuleDescriptionRollback(String moduleId, String prevDescription);

    Boolean softDeleteRollback(String id);
}
