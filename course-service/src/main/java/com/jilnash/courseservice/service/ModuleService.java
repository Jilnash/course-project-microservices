package com.jilnash.courseservice.service;

import com.jilnash.courseservice.model.Module;

import java.util.List;

public interface ModuleService {

    List<Module> getModules(String name, Long courseId);

    Module getModule(Long id, Long courseId);

    Module saveModule(Module module);
}
