package com.jilnash.courseservice.aspect;

import com.jilnash.courseservice.history.EntityKey;
import com.jilnash.courseservice.history.EntityValue;
import com.jilnash.courseservice.service.task.TaskService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class TaskServiceAspect {

    private final Map<EntityKey, EntityValue> entityHistory;

    private final TaskService taskService;

    public TaskServiceAspect(Map<EntityKey, EntityValue> entityHistory,
                             TaskService taskService) {
        this.entityHistory = entityHistory;
        this.taskService = taskService;
    }

    @Before(
            value = "execution(* com.jilnash.courseservice.service.task.TaskServiceImpl.updateTaskPrerequisites(..)) " +
                    "&& args(*, *, taskId, ..)",
            argNames = " taskId"
    )
    public void beforeUpdatePrerequisites(String taskId) {
        entityHistory.put(
                new EntityKey(taskId, "prerequisites"),
                new EntityValue(taskService.getTaskPrerequisites(taskId))
        );
    }

    @Before(
            value = "execution(* com.jilnash.courseservice.service.task.TaskServiceImpl.updateTaskSuccessors(..)) " +
                    "&& args(*, *, taskId, ..)",
            argNames = "taskId"
    )
    public void beforeUpdateSuccessors(String taskId) {
        entityHistory.put(
                new EntityKey(taskId, "successors"),
                new EntityValue(taskService.getTaskSuccessors(taskId))
        );
    }
}
