package com.jilnash.courseservice.service;

import com.jilnash.courseservice.model.Module;
import com.jilnash.courseservice.repo.ModuleRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;

public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepo moduleRepo;

    @Override
    public List<Module> getModules(String name, Long courseId) {
        return moduleRepo.findByNameStartingWithAndCourseId(name, courseId);
    }

    @Override
    public Module getModule(Long id, Long courseId) {
        return moduleRepo
                .findByIdAndCourseId(id, courseId)
                .orElseThrow(() -> new NoSuchElementException("Module not found with id: " + id + " in course id: " + courseId));
    }

    @Override
    public Module saveModule(Module module) {
        return moduleRepo.save(module);
    }
}
