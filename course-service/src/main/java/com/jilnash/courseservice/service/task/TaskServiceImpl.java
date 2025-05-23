package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.client.FileClient;
import com.jilnash.courseservice.client.ProgressGrpcClient;
import com.jilnash.courseservice.client.TaskReqGrpcClient;
import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.dto.task.TaskGraphDTO;
import com.jilnash.courseservice.dto.task.TaskGraphEdgeDTO;
import com.jilnash.courseservice.dto.task.TaskUpdateDTO;
import com.jilnash.courseservice.model.Task;
import com.jilnash.courseservice.repo.TaskRepo;
import com.jilnash.courseservice.service.module.ModuleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;

    private final ModuleServiceImpl moduleService;

    private final FileClient fileClient;

    private final TaskReqGrpcClient taskReqGrpcClient;

    private final ProgressGrpcClient progressGrpcClient;

    private static final String TASK_STORAGE_BUCKET = "course-project-tasks";

    @Override
    @Cacheable(value = "taskLists", key = "#moduleId")
    public List<Task> getTasks(String courseId, String moduleId, String title) {

        moduleService.validateModuleExistsInCourse(moduleId, courseId);

        return taskRepo.findAllByTitleStartingWithAndModule_IdAndModule_Course_Id(title, moduleId, courseId);
    }

    @Override
    @Cacheable(value = "tasks", key = "#id")
    public Task getTask(String courseId, String moduleId, String id) {

        moduleService.validateModuleExistsInCourse(moduleId, courseId);

        return taskRepo
                .findByIdAndModule_IdAndModule_Course_Id(id, moduleId, courseId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public Task getTask(String id) {
        return taskRepo
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Cacheable(value = "taskGraphs", key = "#moduleId")
    public TaskGraphDTO getTaskGraph(String courseId, String moduleId) {

        var tasks = taskRepo.findAllByModule_IdAndModule_Course_Id(moduleId, courseId);

        return TaskGraphDTO.builder()
                .nodes(tasks.stream().map(Task::getId).toList())
                .edges(tasks.stream()
                        .flatMap(task -> task.getSuccessors().stream()
                                .map(successor -> new TaskGraphEdgeDTO(task.getId(), successor.getId())))
                        .toList())
                .build();
    }

    @Override
    @CacheEvict(value = {"taskLists", "taskGraphs", "tasks"}, key = "#task.moduleId")
    public Task create(TaskCreateDTO task) {

        moduleService.validateModuleExistsInCourse(task.getModuleId(), task.getCourseId());

        Set<String> prereqsAndSuccessorIds =
                mergePrerequisitesAndSuccessors(task.getPrerequisiteTasksIds(), task.getSuccessorTasksIds());

        task.setTaskId(UUID.randomUUID().toString());
        task.setVideoLink(task.getVideoFile().getOriginalFilename());

        taskReqGrpcClient.setTaskRequirements(task, task.getTaskId());

        fileClient.uploadFiles(TASK_STORAGE_BUCKET, getTaskVideoPath(task.getTaskId()), List.of(task.getVideoFile()));

        if (prereqsAndSuccessorIds.isEmpty())
            return createFirstTaskInModule(task);
        else
            return createTaskWithPrerequisitesAndSuccessors(task, prereqsAndSuccessorIds);
    }

    private Set<String> mergePrerequisitesAndSuccessors(Set<String> prereqs, Set<String> successors) {
        return Stream.concat(prereqs.stream(), successors.stream()).collect(Collectors.toSet());
    }

    private Task createFirstTaskInModule(TaskCreateDTO task) {

        // throw exception if module already contains tasks
        if (moduleService.hasAtLeastOneTask(task.getModuleId()))
            throw new RuntimeException("Task should be linked with other tasks");

        return taskRepo.createTaskWithoutRelationships(task)
                .orElseThrow(() -> new RuntimeException("Error creating first task"));
    }

    private Task createTaskWithPrerequisitesAndSuccessors(TaskCreateDTO task, Set<String> prereqsAndSuccessorIds) {

        //prerequisites and successors should be distinct
        validatePrerequisitesAndSuccessorsDisjoint(task.getPrerequisiteTasksIds(), task.getSuccessorTasksIds());

        //new task should be connected to tasks only in the same module
        moduleService.validateModuleContainsAllTasks(task.getModuleId(), prereqsAndSuccessorIds);

        taskRepo.deleteTaskRelationshipsByTaskIdLinks(task.getRemoveRelationshipIds());

        progressGrpcClient.updateProgresses(task.getSuccessorTasksIds(), task.getTaskId());

        Task newTask = taskRepo.createTaskWithoutRelationships(task)
                .orElseThrow(() -> new RuntimeException("Error creating task"));
        taskRepo.connectTaskToPrerequisites(task);
        taskRepo.connectTaskToSuccessors(task);

        return newTask;
    }

    private void validatePrerequisitesAndSuccessorsDisjoint(Set<String> prereqs, Set<String> successors) {
        if (!Collections.disjoint(prereqs, successors))
            throw new NoSuchElementException("Pre-requisites and successor tasks should be distinct");
    }

    private String getTaskVideoPath(String taskId) {
        return "task-" + taskId + "/video";
    }

    @Override
    @CacheEvict(value = {"tasks"}, key = "#task.id")
    public Boolean update(TaskUpdateDTO task) {

        // check if task exists
        taskRepo
                .findByIdAndModule_IdAndModule_Course_Id(task.getId(), task.getModuleId(), task.getCourseId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepo.updateTaskData(task.getId(), task.getTitle(), task.getDescription(), task.getVideoLink());

        return true;
    }

    public List<String> getTaskPrerequisites(String courseId, String moduleId, String taskId) {

        return getTask(courseId, moduleId, taskId).getPrerequisites().stream().map(Task::getId).toList();
    }

    public List<String> getTaskPrerequisites(String taskId) {
        return getTask(taskId).getPrerequisites().stream().map(Task::getId).toList();
    }

    @Caching(evict = {
            @CacheEvict(value = {"taskLists", "taskGraphs"}, key = "#moduleId", allEntries = true),
            @CacheEvict(value = "tasks", key = "#taskId")
    })
    public Boolean updateTaskPrerequisite(String courseId, String moduleId, String taskId, Set<String> prerequisiteIds) {

        moduleService.validateModuleContainsAllTasks(moduleId, prerequisiteIds);
        Task task = getTask(courseId, moduleId, taskId);

        task.getPrerequisites().clear();
        task.getPrerequisites().addAll(taskRepo.findAllByIdIn(prerequisiteIds));

        taskRepo.save(task);

        return true;
    }

    @Caching(evict = {
            @CacheEvict(value = "taskLists", key = "#moduleId"),
            @CacheEvict(value = "tasks", key = "#taskId")
    })
    public Boolean updateTaskTitle(String courseId, String moduleId, String taskId, String title) {
        taskRepo.updateTaskTitle(courseId, moduleId, taskId, title);
        return true;
    }

    @Caching(evict = {
            @CacheEvict(value = "taskLists", key = "#moduleId"),
            @CacheEvict(value = "tasks", key = "#taskId")
    })
    public Boolean updateTaskDescription(String courseId, String moduleId, String taskId, String description) {
        taskRepo.updateTaskDescription(courseId, moduleId, taskId, description);
        return true;
    }

    public String getTaskCourseId(String taskId) {
        return taskRepo
                .getTaskCourseId(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
    }

    @Caching(evict = {
            @CacheEvict(value = "taskLists", key = "#moduleId"),
            @CacheEvict(value = "tasks", key = "#id")
    })
    public Boolean updateTaskVideo(String courseId, String moduleId, String id, MultipartFile video) {

        getTask(courseId, moduleId, id);

        fileClient.uploadFiles("course-project-tasks", "task-" + id + "\\video", List.of(video));

        taskRepo.updateTaskVideoLink(courseId, moduleId, id, video.getOriginalFilename());

        return true;
    }

    @Caching(evict = {
            @CacheEvict(value = "taskLists", key = "#moduleId", allEntries = true),
            @CacheEvict(value = "tasks", key = "#id")
    })
    public Boolean updateTaskIsPublic(String courseId, String moduleId, String id, Boolean isPublic) {

        taskRepo.updateTaskIsPublic(courseId, moduleId, id, isPublic);

        return true;
    }
}
