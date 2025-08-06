package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.repo.TaskRepo;
import org.springframework.stereotype.Component;

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

    public TaskServiceRollbackImpl(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
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
    public void rollbackTaskPrerequisitesUpdate(String courseId, String moduleId, String taskId, Set<String> oldPrerequisites) {
        taskRepo.disconnectTaskFromPrerequisites(taskId);//todo: fix
        taskRepo.connectTaskToPrerequisites(taskId, oldPrerequisites);
    }

    @Override
    public void rollbackTaskSuccessorsUpdate(String courseId, String moduleId, String taskId, Set<String> oldSuccessors) {
        taskRepo.disconnectTaskFromSuccessors(taskId);//todo: fix
        taskRepo.connectTaskToSuccessors(taskId, oldSuccessors);
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
