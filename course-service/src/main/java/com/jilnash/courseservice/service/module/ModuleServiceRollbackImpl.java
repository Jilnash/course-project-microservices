package com.jilnash.courseservice.service.module;

import com.jilnash.courseservice.history.EntityKey;
import com.jilnash.courseservice.history.EntityValue;
import com.jilnash.courseservice.repo.ModuleRepo;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class ModuleServiceRollbackImpl implements ModuleServiceRollback {

    private final ModuleRepo moduleRepo;

    private final Map<EntityKey, EntityValue> entityHistory;

    public ModuleServiceRollbackImpl(ModuleRepo moduleRepo,
                                     Map<EntityKey, EntityValue> entityHistory) {
        this.moduleRepo = moduleRepo;
        this.entityHistory = entityHistory;
    }


    private <T> T getPreviousValue(String entityId, String fieldName, Class<T> tClass) {
        Object value = entityHistory.get(new EntityKey(entityId, fieldName)).value();
        return tClass.cast(value);
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
    public Boolean updateModuleNameRollback(String moduleId) {
        String prevName = getPreviousValue(moduleId, "name", String.class);
        moduleRepo.updateModuleName(moduleId, prevName);
        return true;
    }

    @Override
    public Boolean updateModuleDescriptionRollback(String moduleId) {
        String prevDescription = getPreviousValue(moduleId, "description", String.class);
        moduleRepo.updateModuleDescription(moduleId, prevDescription);
        return true;
    }

    @Override
    public Boolean softDeleteRollback(String id) {
        moduleRepo.updateModuleDeletedAt(id, null);
        return true;
    }
}
