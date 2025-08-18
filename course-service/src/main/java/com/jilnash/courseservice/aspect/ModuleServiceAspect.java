package com.jilnash.courseservice.aspect;

import com.jilnash.courseservice.history.EntityKey;
import com.jilnash.courseservice.history.EntityValue;
import com.jilnash.courseservice.service.module.ModuleService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class ModuleServiceAspect {

    private final Map<EntityKey, EntityValue> entityHistory;

    private final ModuleService moduleService;

    public ModuleServiceAspect(Map<EntityKey, EntityValue> entityHistory, ModuleService moduleService) {
        this.entityHistory = entityHistory;
        this.moduleService = moduleService;
    }

    @Before(
            value = "execution(* com.jilnash.courseservice.service.module.ModuleServiceImpl.updateModuleName(..))" +
                    " && args(*, id, *)",
            argNames = "id"
    )
    public void beforeUpdateModuleName(String id) {
        String name = moduleService.getModule(id).getName();
        entityHistory.put(new EntityKey(id, "name"), new EntityValue(name));
    }

    @Before(
            value = "execution(* com.jilnash.courseservice.service.module.ModuleServiceImpl.updateModuleDescription(..))" +
                    " && args(*, id, *)",
            argNames = "id"
    )
    public void beforeUpdateModuleDescription(String id) {
        String description = moduleService.getModule(id).getDescription();
        entityHistory.put(new EntityKey(id, "description"), new EntityValue(description));
    }
}
