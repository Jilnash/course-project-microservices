package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.history.EntityKey;
import com.jilnash.courseservice.history.EntityValue;
import com.jilnash.courseservice.repo.TaskRepo;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Implementation of the TaskServiceRollback interface that provides rollback mechanisms
 * for various task-level operations within a course module. This implementation handles
 * rollbacks for task creation, updates, deletion, and attributes changes to ensure
 * data consistency in case of errors or unexpected failures.
 */
@Component
public class TaskServiceRollbackImpl implements TaskServiceRollback {

    private final TaskRepo taskRepo;

    private final Map<EntityKey, EntityValue> entityHistory;

    public TaskServiceRollbackImpl(TaskRepo taskRepo, Map<EntityKey,
            EntityValue> entityHistory) {
        this.taskRepo = taskRepo;
        this.entityHistory = entityHistory;
    }

    private <T> T getPreviousValue(String entityId, String fieldName, Class<T> tClass) {
        Object value = entityHistory.get(new EntityKey(entityId, fieldName)).value();
        return tClass.cast(value);
    }


    @Override
    public void rollbackTaskCreate(String courseId, String moduleId, String taskId) {
        taskRepo.detachDeleteTask(courseId, moduleId, taskId);
    }

    @Override
    public void rollbackTaskTitleUpdate(String courseId, String moduleId, String taskId) {
        String prevTitle = getPreviousValue(taskId, "title", String.class);
        taskRepo.updateTaskTitle(courseId, moduleId, taskId, prevTitle);
    }

    @Override
    public void rollbackTaskDescriptionUpdate(String courseId, String moduleId, String taskId) {
        String prevDescription = getPreviousValue(taskId, "description", String.class);
        taskRepo.updateTaskDescription(courseId, moduleId, taskId, prevDescription);
    }

    @Override
    public void rollbackTaskVideoFileNameUpdate(String courseId, String moduleId, String taskId) {
        String prevVideoFileName = getPreviousValue(taskId, "videoFileName", String.class);
        taskRepo.updateTaskVideoLink(courseId, moduleId, taskId, prevVideoFileName);
    }

    @Override
    public void rollbackTaskPrerequisitesUpdate(String taskId) {
        Set<String> prevPrerequisites = getPreviousValue(taskId, "prerequisites", Set.class);
        taskRepo.disconnectTaskFromPrerequisites(taskId);
        taskRepo.connectTaskToPrerequisites(taskId, prevPrerequisites);
    }

    @Override
    public void rollbackTaskSuccessorsUpdate(String taskId) {
        Set<String> prevSuccessors = getPreviousValue(taskId, "successors", Set.class);
        taskRepo.disconnectTaskFromSuccessors(taskId);
        taskRepo.connectTaskToSuccessors(taskId, prevSuccessors);
    }

    @Override
    public void rollbackTaskIsPublicUpdate(String courseId, String moduleId, String taskId) {
        Boolean prevIsPublic = getPreviousValue(taskId, "isPublic", Boolean.class);
        taskRepo.updateTaskIsPublic(courseId, moduleId, taskId, prevIsPublic);
    }

    @Override
    public void rollbackTaskSoftDelete(String courseId, String moduleId, String taskId) {
        taskRepo.findById(taskId).ifPresentOrElse(task -> {
                    task.setDeletedAt(null);
                    taskRepo.save(task);
                }, () -> {
                    throw new IllegalArgumentException("Task not found for rollback soft softDeleteModule: " + taskId);
                }
        );
    }

    @Override
    public void rollbackTaskPostingIntervalUpdate(String courseId, String moduleId, String taskId) {
        Integer prevDayInterval = getPreviousValue(taskId, "postingInterval", Integer.class);
        taskRepo.updateTaskHwPostingInterval(courseId, moduleId, taskId, prevDayInterval);
    }
}
