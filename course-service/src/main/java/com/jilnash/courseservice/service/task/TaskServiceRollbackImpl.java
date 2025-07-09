package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.repo.TaskRepo;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * Implementation of the TaskServiceRollback interface that provides rollback mechanisms
 * for various task-level operations within a course module. This implementation handles
 * rollbacks for task creation, updates, deletion, and attributes changes to ensure
 * data consistency in case of errors or unexpected failures.
 */
@RequiredArgsConstructor
public class TaskServiceRollbackImpl implements TaskServiceRollback {

    private final TaskRepo taskRepo;

    @Override
    public void rollbackTaskCreate(String courseId, String moduleId, String taskId) {

    }

    @Override
    public void rollbackTaskTitleUpdate(String courseId, String moduleId, String taskId, String oldTitle) {
        taskRepo.findById(taskId).ifPresent(task -> {
            task.setTitle(oldTitle);
            taskRepo.save(task);
        });
    }

    @Override
    public void rollbackTaskDescriptionUpdate(String courseId, String moduleId, String taskId, String oldDescription) {
        taskRepo.findById(taskId).ifPresent(task -> {
            task.setDescription(oldDescription);
            taskRepo.save(task);
        });
    }

    @Override
    public void rollbackTaskVideoFileNameUpdate(String courseId, String moduleId, String taskId, String oldVideoFileName) {
        taskRepo.findByIdAndModule_IdAndModule_Course_Id(taskId, moduleId, courseId)
                .ifPresent(task -> {
                    task.setVideoFileName(oldVideoFileName);
                    taskRepo.save(task);
                });
    }

    @Override
    public void rollbackTaskPrerequisitesUpdate(String courseId, String moduleId, String taskId, Set<String> oldPrerequisites) {
        //todo: disconnect task from current prerequisites
        taskRepo.connectTaskToPrerequisites(taskId, oldPrerequisites);
    }

    @Override
    public void rollbackTaskSuccessorsUpdate(String courseId, String moduleId, String taskId, Set<String> oldSuccessors) {
        //todo: disconnect task from current successors
        taskRepo.connectTaskToSuccessors(taskId, oldSuccessors);
    }

    @Override
    public void rollbackTaskIsPublicUpdate(String courseId, String moduleId, String taskId, boolean oldIsPublic) {
        taskRepo.findByIdAndModule_IdAndModule_Course_Id(taskId, moduleId, courseId)
                .ifPresent(task -> {
                    task.setIsPublic(oldIsPublic);
                    taskRepo.save(task);
                });
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
    public void rollbackTaskHardDelete(String courseId, String moduleId, String taskId) {

    }
}
