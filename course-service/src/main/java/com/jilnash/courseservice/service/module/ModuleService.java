package com.jilnash.courseservice.service.module;

import com.jilnash.courseservice.dto.module.ModuleCreateDTO;
import com.jilnash.courseservice.dto.module.ModuleUpdateDTO;
import com.jilnash.courseservice.model.Module;

import java.util.List;

public interface ModuleService {

    List<Module> getModulesInCourse(String courseId);

    Module getModuleByCourse(String courseId, String id);

    Module create(ModuleCreateDTO module);

    Module update(ModuleUpdateDTO module);

    Module delete(String id);
}
