package com.jilnash.courseservice.service.module;

import com.jilnash.courseservice.model.Module;
import com.jilnash.courseservicedto.dto.module.ModuleCreateDTO;

import java.util.List;
import java.util.Set;

public interface ModuleService {

    List<Module> getModules(String courseId);

    Module getModule(String id);

    Module getModule(String courseId, String id);

    Module createModule(ModuleCreateDTO module);

    Boolean updateModuleName(String courseId, String id, String name);

    Boolean updateModuleDescription(String courseId, String id, String description);

    Boolean softDeleteModule(String id);

    Boolean hardDeleteModule(String id);

    void validateModuleExists(String courseId, String id);

    void validateModuleContainsAllTasks(String id, Set<String> taskIds);

    Boolean hasAtLeastOneTask(String id);
}
