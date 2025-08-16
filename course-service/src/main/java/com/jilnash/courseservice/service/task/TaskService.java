package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.model.Task;
import com.jilnash.courseservicedto.dto.task.TaskCreateDTO;
import com.jilnash.courseservicedto.dto.task.TaskGraph;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface TaskService {

    List<Task> getTasks(String courseId, String moduleId, String name);

    TaskGraph getTasksAsGraph(String courseId, String moduleId);

    Task getTask(String id);

    Task getTask(String courseId, String moduleId, String id);

    String getTaskCourseId(String id);

    String getTaskModuleId(String id);

    String getTaskTitle(String id);

    String getTaskDescription(String id);

    String getTaskVideoFileName(String id);

    Boolean getTaskIsPublic(String id);

    Integer getTaskHwPostingInterval(String id);

    Set<String> getTaskPrerequisites(String id);

    Set<String> getTaskSuccessors(String id);

    Task createTask(TaskCreateDTO taskCreateDTO);

    Boolean updateTaskTitle(String courseId, String moduleId, String taskId, String title);

    Boolean updateTaskDescription(String courseId, String moduleId, String taskId, String description);

    Boolean updateTaskVideoFileName(String courseId, String moduleId, String id, String videoFileName);

    Boolean updateTaskIsPublic(String courseId, String moduleId, String id, Boolean isPublic);

    Boolean updateTaskHwPostingInterval(String courseId, String moduleId, String id, Integer hwPostingInterval);

    Boolean updateTaskPrerequisites(String courseId, String moduleId, String taskId, Set<String> prerequisiteTasksIds);

    Boolean updateTaskSuccessors(String courseId, String moduleId, String taskId, Set<String> successorTasksIds);

    Boolean softDeleteTask(String courseId, String moduleId, String taskId);

    Boolean hardDeleteTask(String courseId, String moduleId, String taskId);
}
