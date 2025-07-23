package com.jilnash.courseservicesaga.service.task;

import com.jilnash.courseservicedto.dto.task.TaskCreateDTO;
import com.jilnash.courseservicedto.dto.task.TaskGraph;
import com.jilnash.courseservicedto.dto.task.TaskResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public interface TaskServiceSaga {

    List<TaskResponse> getTasks(String courseId, String moduleId, String name);

    TaskGraph getTasksAsGraph(String courseId, String moduleId) throws ExecutionException, InterruptedException;

    TaskResponse getTask(String id) throws ExecutionException, InterruptedException;

    TaskResponse getTask(String courseId, String moduleId, String id);

    String getTaskTitle(String id);

    String getTaskDescription(String id);

    String getTaskVideoFilePresignedUrl(String id);

    Boolean getTaskIsPublic(String id);

    void createTask(TaskCreateDTO taskCreateDTO);

    void updateTaskTitle(String courseId, String moduleId, String taskId, String title);

    void updateTaskDescription(String courseId, String moduleId, String taskId, String description);

    void updateTaskVideoFile(String courseId, String moduleId, String id, MultipartFile videoFileName);

    void updateTaskIsPublic(String courseId, String moduleId, String id, Boolean isPublic);

    void updateTaskHwPostingInterval(String courseId, String moduleId, String id, Integer hwPostingInterval);

    void updateTaskPrerequisites(String courseId, String moduleId, String taskId, Set<String> prerequisiteTasksIds);

    void updateTaskSuccessors(String courseId, String moduleId, String taskId, Set<String> successorTasksIds);

    void softDeleteTask(String courseId, String moduleId, String taskId);

    void hardDeleteTask(String courseId, String moduleId, String taskId);
}
