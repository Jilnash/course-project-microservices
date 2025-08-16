package com.jilnash.courseservice.service.task;

import org.springframework.stereotype.Component;

@Component
public interface TaskServiceRollback {

    void rollbackTaskCreate(String courseId, String moduleId, String taskId);

    void rollbackTaskTitleUpdate(String courseId, String moduleId, String taskId, String oldTitle);

    void rollbackTaskDescriptionUpdate(String courseId, String moduleId, String taskId, String oldDescription);

    void rollbackTaskVideoFileNameUpdate(String courseId, String moduleId, String taskId, String oldVideoFileName);

    void rollbackTaskIsPublicUpdate(String courseId, String moduleId, String taskId, boolean oldIsPublic);

    void rollbackTaskPrerequisitesUpdate(String taskId);

    void rollbackTaskSuccessorsUpdate(String taskId);

    void rollbackTaskSoftDelete(String courseId, String moduleId, String taskId);

    void rollbackTaskPostingIntervalUpdate(String courseId, String moduleI, String taskId, int interval);
}
