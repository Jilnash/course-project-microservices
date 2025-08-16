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

    @Override
    public void rollbackTaskCreate(String courseId, String moduleId, String taskId) {
        taskRepo.detachDeleteTask(courseId, moduleId, taskId);
    }

    @Override
    public void rollbackTaskTitleUpdate(String courseId, String moduleId, String taskId, String oldTitle) {
        taskRepo.updateTaskTitle(courseId, moduleId, taskId, oldTitle);
    }

    @Override
    public void rollbackTaskDescriptionUpdate(String courseId, String moduleId, String taskId, String oldDescription) {
        taskRepo.updateTaskDescription(courseId, moduleId, taskId, oldDescription);
    }

    @Override
    public void rollbackTaskVideoFileNameUpdate(String courseId, String moduleId, String taskId, String oldVideoFileName) {
        taskRepo.updateTaskVideoLink(courseId, moduleId, taskId, oldVideoFileName);
    }

    @Override
    public void rollbackTaskPrerequisitesUpdate(String taskId) {
        taskRepo.disconnectTaskFromPrerequisites(taskId);

        Set<String> prevPrerequisites = (Set<String>) entityHistory.get(new EntityKey(taskId, "prerequisites")).value();
        taskRepo.connectTaskToPrerequisites(taskId, prevPrerequisites);
    }

    @Override
    public void rollbackTaskSuccessorsUpdate(String taskId) {
        taskRepo.disconnectTaskFromSuccessors(taskId);

        Set<String> prevSuccessors = (Set<String>) entityHistory.get(new EntityKey(taskId, "successors")).value();
        taskRepo.connectTaskToSuccessors(taskId, prevSuccessors);
    }

    @Override
    public void rollbackTaskIsPublicUpdate(String courseId, String moduleId, String taskId, boolean oldIsPublic) {
        taskRepo.updateTaskIsPublic(courseId, moduleId, taskId, oldIsPublic);
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
    public void rollbackTaskPostingIntervalUpdate(String courseId, String moduleId, String taskId, int interval) {
        taskRepo.updateTaskHwPostingInterval(courseId, moduleId, taskId, interval);
    }
}
