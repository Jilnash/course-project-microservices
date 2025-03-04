package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.client.FileClient;
import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.dto.task.TaskGraphDTO;
import com.jilnash.courseservice.dto.task.TaskGraphEdgeDTO;
import com.jilnash.courseservice.dto.task.TaskUpdateDTO;
import com.jilnash.courseservice.model.Task;
import com.jilnash.courseservice.repo.TaskRepo;
import com.jilnash.courseservice.service.module.ModuleServiceImpl;
import com.jilnash.progressservice.InsertTaskToProgressRequest;
import com.jilnash.progressservice.ProgressServiceGrpc;
import com.jilnash.taskrequirementsservice.SetTaskRequirementsRequest;
import com.jilnash.taskrequirementsservice.TaskRequirementsServiceGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
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

    private static final String TASK_STORAGE_BUCKET = "course-project-tasks";

    @GrpcClient("progress-client")
    private ProgressServiceGrpc.ProgressServiceBlockingStub progressGrpcClient;

    @GrpcClient("task-requirements-client")
    private TaskRequirementsServiceGrpc.TaskRequirementsServiceBlockingStub taskRequirementsGrpcClient;

    @Override
    @Cacheable(value = "taskLists", key = "#moduleId")
    public List<Task> getTasks(String courseId, String moduleId, String title) {

        return taskRepo.findAllByTitleStartingWithAndModule_IdAndModule_Course_Id(title, moduleId, courseId);
    }

    @Override
    @Cacheable(value = "tasks", key = "#id")
    public Task getTask(String courseId, String moduleId, String id) {

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
    public Boolean create(TaskCreateDTO task) {

        moduleService.validateModuleExistsInCourse(task.getModuleId(), task.getCourseId());

        Set<String> prereqsAndSuccessorIds =
                mergePrerequisitesAndSuccessors(task.getPrerequisiteTasksIds(), task.getSuccessorTasksIds());

        task.setTaskId(UUID.randomUUID().toString());
        task.setVideoLink(task.getVideoFile().getOriginalFilename());

        if (prereqsAndSuccessorIds.isEmpty())
            createFirstTaskInModule(task);
        else
            createTaskWithPrerequisitesAndSuccessors(task, prereqsAndSuccessorIds);

        setTaskRequirements(task, task.getTaskId());

        fileClient.uploadFiles(TASK_STORAGE_BUCKET, getTaskVideoPath(task.getTaskId()), List.of(task.getVideoFile()));

        return true;
    }

    private Set<String> mergePrerequisitesAndSuccessors(Set<String> prereqs, Set<String> successors) {
        return Stream.concat(prereqs.stream(), successors.stream()).collect(Collectors.toSet());
    }

    private void createFirstTaskInModule(TaskCreateDTO task) {

        // throw exception if module already contains tasks
        if (moduleService.hasAtLeastOneTask(task.getModuleId()))
            throw new RuntimeException("Task should be linked with other tasks");

        taskRepo.createTaskWithoutRelationships(task);
    }

    private void createTaskWithPrerequisitesAndSuccessors(TaskCreateDTO task, Set<String> prereqsAndSuccessorIds) {

        //prerequisites and successors should be distinct
        validatePrerequisitesAndSuccessorsDisjoint(task.getPrerequisiteTasksIds(), task.getSuccessorTasksIds());

        //new task should be connected to tasks only in the same module
        moduleService.validateModuleContainsAllTasks(task.getModuleId(), prereqsAndSuccessorIds);

        taskRepo.deleteTaskRelationshipsByTaskIdLinks(task.getRemoveRelationshipIds());

        taskRepo.createTaskWithoutRelationships(task);
        taskRepo.connectTaskToPrerequisites(task);
        taskRepo.connectTaskToSuccessors(task);

        updateProgresses(task, task.getTaskId());
    }

    private void validatePrerequisitesAndSuccessorsDisjoint(Set<String> prereqs, Set<String> successors) {
        if (!Collections.disjoint(prereqs, successors))
            throw new NoSuchElementException("Pre-requisites and successor tasks should be distinct");
    }

    private void updateProgresses(TaskCreateDTO task, String generatedTaskId) {
        // the new task should be included in progress
        // of all students who have completed successors the new task
        progressGrpcClient.insertTaskToProgress(InsertTaskToProgressRequest.newBuilder()
                .setNewTaskId(generatedTaskId)
                .addAllCompletedTaskIds(task.getSuccessorTasksIds())
                .build()
        );
    }

    @Async
    protected void setTaskRequirements(TaskCreateDTO task, String generatedTaskId) {
        taskRequirementsGrpcClient.setTaskRequirements(
                SetTaskRequirementsRequest.newBuilder()
                        .setTaskId(generatedTaskId)
                        .addAllRequirements(task.getFileRequirements().stream()
                                .map(req -> com.jilnash.taskrequirementsservice.Requirement.newBuilder()
                                        .setContentType(req.contentType())
                                        .setCount(req.count())
                                        .build())
                                .toList())
                        .build()
        );
    }

    private String getTaskVideoPath(String taskId) {
        return "task-" + taskId + "/video";
    }

    @Override
    public Boolean update(TaskUpdateDTO task) {

        // check if task exists
        getTask(task.getId(), task.getModuleId(), task.getCourseId());

        taskRepo.updateTaskData(task.getId(), task.getTitle(), task.getDescription(), task.getVideoLink());

        return true;
    }

    public List<String> getTaskPrerequisites(String courseId, String moduleId, String taskId) {

        return getTask(courseId, moduleId, taskId).getPrerequisites().stream().map(Task::getId).toList();
    }

    public List<String> getTaskPrerequisites(String taskId) {
        return getTask(taskId).getPrerequisites().stream().map(Task::getId).toList();
    }

    public Boolean updateTaskPrerequisite(String courseId, String moduleId, String taskId, Set<String> prerequisiteIds) {

        moduleService.validateModuleContainsAllTasks(moduleId, prerequisiteIds);
        Task task = getTask(courseId, moduleId, taskId);

        task.getPrerequisites().clear();
        task.getPrerequisites().addAll(taskRepo.findAllByIdIn(prerequisiteIds));

        taskRepo.save(task);

        return true;
    }

    public Boolean updateTaskTitle(String courseId, String moduleId, String taskId, String title) {
        taskRepo.updateTaskTitle(courseId, moduleId, taskId, title);
        return true;
    }

    public Boolean updateTaskDescription(String courseId, String moduleId, String taskId, String description) {
        taskRepo.updateTaskDescription(courseId, moduleId, taskId, description);
        return true;
    }

    public String getTaskCourseId(String taskId) {
        return taskRepo
                .getTaskCourseId(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
    }

    public Boolean updateTaskVideo(String courseId, String moduleId, String id, MultipartFile video) {

        getTask(courseId, moduleId, id);

        fileClient.uploadFiles(
                "course-project-tasks",
                "task-" + id + "\\video",
                List.of(video)
        );

        taskRepo.updateTaskVideoLink(courseId, moduleId, id, video.getOriginalFilename());

        return true;
    }

    public Boolean updateTaskIsPublic(String courseId, String moduleId, String id, Boolean isPublic) {

        taskRepo.updateTaskIsPublic(courseId, moduleId, id, isPublic);

        return true;
    }
}
