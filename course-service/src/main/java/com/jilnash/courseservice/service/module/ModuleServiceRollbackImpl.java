package com.jilnash.courseservice.service.module;

import com.jilnash.courseservice.repo.ModuleRepo;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class ModuleServiceRollbackImpl implements ModuleServiceRollback {

    private final ModuleRepo moduleRepo;

    public ModuleServiceRollbackImpl(ModuleRepo moduleRepo) {
        this.moduleRepo = moduleRepo;
    }

    @Override
    public Boolean createModuleRollback(String createdModule) {
        return moduleRepo.findModuleById(createdModule)
                .map(module -> {
                    moduleRepo.deleteById(createdModule);
                    return true;
                })
                .orElseThrow(() -> new NoSuchElementException("Module not found with id: " + createdModule));
    }

    @Override
    public Boolean updateModuleNameRollback(String moduleId, String prevName) {
        moduleRepo.updateModuleName(moduleId, prevName);
        return true;
    }

    @Override
    public Boolean updateModuleDescriptionRollback(String moduleId, String prevDescription) {
        moduleRepo.updateModuleDescription(moduleId, prevDescription);
        return true;
    }

    @Override
    public Boolean softDeleteRollback(String id) {
        moduleRepo.updateModuleDeletedAt(id, null);
        return true;
    }
}
