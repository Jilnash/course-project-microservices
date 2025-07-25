package com.jilnash.courseservicesaga.service.task;

import com.jilnash.courseservicedto.dto.task.TaskGraph;
import com.jilnash.courseservicedto.dto.task.TaskResponse;
import com.jilnash.courseservicesaga.dto.TaskSagaCreateDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
public interface TaskServiceSaga {

    List<TaskResponse> getTasks(String userId, String courseId, String moduleId, String name);

    TaskGraph getTasksAsGraph(String courseId, String moduleId);

    TaskResponse getTask(String userId, String id);

    TaskResponse getTask(String userId, String courseId, String moduleId, String id);

    String getTaskTitle(String userId, String id);

    String getTaskDescription(String userId, String id);

    String getTaskVideoFilePresignedUrl(String userId, String id);

    Boolean getTaskIsPublic(String userId, String id);

    void createTask(TaskSagaCreateDTO taskCreateDTO);

    void updateTaskTitle(String teacherId, String courseId, String moduleId, String taskId, String title);

    void updateTaskDescription(String teacherId, String courseId, String moduleId, String taskId, String description);

    void updateTaskVideoFile(String teacherId, String courseId, String moduleId, String id, MultipartFile videoFileName);

    void updateTaskIsPublic(String teacherId, String courseId, String moduleId, String id, Boolean isPublic);

    void updateTaskHwPostingInterval(String teacherId, String courseId, String moduleId, String id, Integer hwPostingInterval);

    void updateTaskPrerequisites(String teacherId, String courseId, String moduleId, String taskId, Set<String> prerequisiteTasksIds);

    void updateTaskSuccessors(String teacherId, String courseId, String moduleId, String taskId, Set<String> successorTasksIds);

    void softDeleteTask(String teacherId, String courseId, String moduleId, String taskId);

    void hardDeleteTask(String teacherId, String courseId, String moduleId, String taskId);
}
