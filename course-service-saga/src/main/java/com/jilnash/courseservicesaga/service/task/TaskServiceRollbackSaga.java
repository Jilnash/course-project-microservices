package com.jilnash.courseservicesaga.service.task;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Component
public interface TaskServiceRollbackSaga {

    void rollbackTaskCreate(String courseId, String moduleId, String taskId);

    void rollbackTaskTitleUpdate(String courseId, String moduleId, String taskId, String oldTitle);

    void rollbackTaskDescriptionUpdate(String courseId, String moduleId, String taskId, String oldDescription);

    void rollbackTaskVideoFileUpdate(String courseId, String moduleId, String taskId, MultipartFile oldVideoFile);

    void rollbackTaskIsPublicUpdate(String courseId, String moduleId, String taskId, boolean oldIsPublic);

    void rollbackTaskPrerequisitesUpdate(String courseId, String moduleId, String taskId, Set<String> oldPrerequisites);

    void rollbackTaskSuccessorsUpdate(String courseId, String moduleId, String taskId, Set<String> oldSuccessors);

    void rollbackTaskSoftDelete(String courseId, String moduleId, String taskId);
}
