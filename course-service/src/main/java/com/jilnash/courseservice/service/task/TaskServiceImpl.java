package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.clients.CourseAccessClient;
import com.jilnash.courseservice.clients.CourseRightsClient;
import com.jilnash.courseservice.dto.task.*;
import com.jilnash.courseservice.mapper.TaskMapper;
import com.jilnash.courseservice.model.Task;
import com.jilnash.courseservice.repo.TaskRepo;
import com.jilnash.courseservice.service.module.ModuleServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;

    private final TaskMapper taskMapper;

    private final ModuleServiceImpl moduleService;

    private final CourseAccessClient courseAccessClient;

    private final CourseRightsClient courseRightsClient;

    public TaskServiceImpl(TaskRepo taskRepo, TaskMapper taskMapper, ModuleServiceImpl moduleService, CourseAccessClient courseAccessClient, CourseRightsClient courseRightsClient) {
        this.taskRepo = taskRepo;
        this.taskMapper = taskMapper;
        this.moduleService = moduleService;
        this.courseAccessClient = courseAccessClient;
        this.courseRightsClient = courseRightsClient;
    }

    @Override
    @Cacheable(value = "taskLists", key = "#moduleId")
    public List<TaskResponseDTO> getTasks(String courseId, String moduleId, String title) {

        if (!courseAccessClient.checkAccess(courseId, "1"))
            throw new NoSuchElementException("Access denied");

        return taskRepo
                .findAllByTitleStartingWithAndModule_IdAndModule_Course_Id(title, moduleId, courseId)
                .stream()
                .map(taskMapper::toTaskResponse)
                .toList();
    }

    @Override
    @Cacheable(value = "tasks", key = "#id")
    public TaskResponseDTO getTask(String courseId, String moduleId, String id) {

        if (!courseAccessClient.checkAccess(courseId, "1"))
            throw new NoSuchElementException("Access denied");

        return taskMapper.toTaskResponse(
                taskRepo.getTaskData(id, moduleId, courseId)
                        .orElseThrow(() -> new RuntimeException("Task not found"))
        );
    }

    @Cacheable(value = "taskGraphs", key = "#moduleId")
    public TaskGraphDTO getTaskGraph(String courseId, String moduleId) {

        var tasks = taskRepo.findAllByModule_IdAndModule_Course_Id(moduleId, courseId);

        return TaskGraphDTO.builder()
                .nodes(tasks.stream().map(Task::getId).toList())
                .edges(tasks.stream()
                        .flatMap(task -> task.getTasks().stream()
                                .map(prerequisite -> new TaskGraphEdgeDTO(task.getId(), prerequisite.getId())))
                        .toList())
                .build();
    }

    @Override
    public Task create(TaskCreateDTO task) {

        if (!courseRightsClient.hasRights(task.getCourseId(), "1", List.of("add")))
            throw new UsernameNotFoundException("Access denied");

        task.setModule(moduleService.getModuleByCourse(task.getCourseId(), task.getModuleId()));

        return taskRepo.save(taskMapper.toNode(task));
    }

    @Override
    public Task update(TaskUpdateDTO task) {

        if (!courseRightsClient.hasRights(task.getCourseId(), "1", List.of("edit")))
            throw new NoSuchElementException("Access denied");

        if (!taskRepo.existsByIdAndModuleIdAndModule_CourseId(task.getId(), task.getModuleId(), task.getCourseId()))
            throw new UsernameNotFoundException("Task not found");

        return taskRepo.updateTaskData(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getVideoLink(),
                task.getAudioRequired(),
                task.getVideoRequired()
        );
    }

    public List<Task> getTaskPrerequisites(String courseId, String moduleId, String taskId) {

        return taskRepo
                .findByIdAndModule_IdAndModule_Course_Id(taskId, moduleId, courseId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"))
                .getTasks();
    }

    public List<TaskResponseDTO> updateTaskPrerequisite(String courseId, String moduleId, String taskId,
                                                        List<String> prerequisiteId) {

        if (!courseRightsClient.hasRights(courseId, "1", List.of("edit")))
            throw new NoSuchElementException("Access denied");

        Task task = taskRepo
                .findByIdAndModule_IdAndModule_Course_Id(taskId, moduleId, courseId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        task.getTasks().clear();
        task.getTasks().addAll(taskRepo.findAllByIdIn(prerequisiteId));

        return taskRepo.save(task).getTasks().stream().map(taskMapper::toTaskResponse).toList();
    }

    public String getTaskCourseId(String taskId) {
        return taskRepo
                .getTaskCourseId(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
    }
}
