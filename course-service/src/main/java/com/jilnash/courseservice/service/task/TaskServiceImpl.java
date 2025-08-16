package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.model.Task;
import com.jilnash.courseservice.repo.TaskRepo;
import com.jilnash.courseservice.service.module.ModuleServiceImpl;
import com.jilnash.courseservicedto.dto.task.TaskCreateDTO;
import com.jilnash.courseservicedto.dto.task.TaskGraph;
import com.jilnash.courseservicedto.dto.task.TaskGraphEdge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;

    private final ModuleServiceImpl moduleService;

    @Override
    public List<Task> getTasks(String courseId, String moduleId, String title) {

        moduleService.validateModuleExists(moduleId, courseId);

        return taskRepo.findAllByTitleStartingWithAndModule_IdAndModule_Course_IdAndDeletedAtIsNull(title, moduleId, courseId);
    }

    @Override
    public TaskGraph getTasksAsGraph(String courseId, String moduleId) {

        var tasks = taskRepo.findAllByModule_IdAndModule_Course_Id(moduleId, courseId);

        return new TaskGraph(
                tasks.stream().map(Task::getId).toList(),
                tasks.stream()
                        .flatMap(task -> task.getSuccessors().stream()
                                .map(successor -> new TaskGraphEdge(task.getId(), successor.getId()))
                        ).toList());
    }

    @Override
    public Task getTask(String id) {
        return taskRepo
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public Task getTask(String courseId, String moduleId, String id) {

        moduleService.validateModuleExists(moduleId, courseId);

        return taskRepo
                .findByIdAndModule_IdAndModule_Course_Id(id, moduleId, courseId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public String getTaskCourseId(String taskId) {
        return taskRepo
                .getTaskCourseId(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
    }

    @Override
    public String getTaskModuleId(String id) {
        return taskRepo
                .getTaskModuleId(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
    }

    @Override
    public String getTaskTitle(String id) {
        return getTask(id).getTitle();
    }

    @Override
    public String getTaskDescription(String id) {
        return getTask(id).getDescription();
    }

    @Override
    public String getTaskVideoFileName(String id) {
        return getTask(id).getVideoFileName();
    }

    @Override
    public Boolean getTaskIsPublic(String id) {
        return getTask(id).getIsPublic();
    }

    @Override
    public Integer getTaskHwPostingInterval(String id) {
        return getTask(id).getHwPostingInterval();
    }

    @Override
    public Set<String> getTaskPrerequisites(String taskId) {

        return getTask(taskId).getPrerequisites().parallelStream().map(Task::getId).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getTaskSuccessors(String taskId) {
        return getTask(taskId).getSuccessors().parallelStream().map(Task::getId).collect(Collectors.toSet());
    }

    @Override
    public Task createTask(TaskCreateDTO task) {

        moduleService.validateModuleExists(task.getModuleId(), task.getCourseId());

        Set<String> prereqsAndSuccessorIds =
                mergePrerequisitesAndSuccessors(task.getPrerequisiteTasksIds(), task.getSuccessorTasksIds());

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
        if (task.getPrerequisiteTasksIds().size() + task.getSuccessorTasksIds().size() != prereqsAndSuccessorIds.size())
            throw new NoSuchElementException("Pre-requisites and successor tasks should be distinct");

        //new task should be connected to tasks only in the same module
        moduleService.validateModuleContainsAllTasks(task.getModuleId(), prereqsAndSuccessorIds);

        Task createdTask = taskRepo.createTaskWithoutRelationships(task)
                .orElseThrow(() -> new RuntimeException("Error creating task"));

        taskRepo.connectTaskToPrerequisites(task.getTaskId(), task.getPrerequisiteTasksIds());
        taskRepo.connectTaskToSuccessors(task.getTaskId(), task.getSuccessorTasksIds());

        return createdTask;
    }

    @Override
    public Boolean updateTaskTitle(String courseId, String moduleId, String taskId, String title) {
        taskRepo.updateTaskTitle(courseId, moduleId, taskId, title);
        return true;
    }

    @Override
    public Boolean updateTaskDescription(String courseId, String moduleId, String taskId, String description) {
        taskRepo.updateTaskDescription(courseId, moduleId, taskId, description);
        return true;
    }

    @Override
    public Boolean updateTaskPrerequisites(String courseId, String moduleId, String taskId, Set<String> prerequisiteIds) {

        moduleService.validateModuleContainsAllTasks(moduleId, prerequisiteIds);
        Task task = getTask(courseId, moduleId, taskId);

        task.getPrerequisites().clear();
        task.getPrerequisites().addAll(taskRepo.findAllByIdIn(prerequisiteIds));

        taskRepo.save(task);

        return true;
    }

    @Override
    public Boolean updateTaskSuccessors(String courseId, String moduleId, String taskId, Set<String> successorIds) {

        Task task = getTask(courseId, moduleId, taskId);
        task.getSuccessors().clear();

        if (successorIds.isEmpty()) {
            taskRepo.save(task);
            return true;
        }
        moduleService.validateModuleContainsAllTasks(moduleId, successorIds);

        task.getSuccessors().addAll(taskRepo.findAllByIdIn(successorIds));

        taskRepo.save(task);

        return true;

    }

    @Override
    public Boolean updateTaskVideoFileName(String courseId, String moduleId, String id, String videoFileName) {

        getTask(courseId, moduleId, id);

//        fileClient.uploadFiles("course-project-tasks", "task-" + id + "\\video", List.of(video));

        taskRepo.updateTaskVideoLink(courseId, moduleId, id, videoFileName);

        return true;
    }

    @Override
    public Boolean updateTaskIsPublic(String courseId, String moduleId, String id, Boolean isPublic) {

        taskRepo.updateTaskIsPublic(courseId, moduleId, id, isPublic);

        return true;
    }

    @Override
    public Boolean updateTaskHwPostingInterval(String courseId, String moduleId, String id, Integer hwPostingInterval) {

        taskRepo.updateTaskHwPostingInterval(courseId, moduleId, id, hwPostingInterval);

        return true;
    }

    @Override
    public Boolean softDeleteTask(String courseId, String moduleId, String taskId) {
        return taskRepo
                .findByIdAndModule_IdAndModule_Course_Id(courseId, moduleId, taskId)
                .map(task -> {
                    task.setDeletedAt(new Date(System.currentTimeMillis()));
                    taskRepo.save(task);
                    return true;
                })
                .orElseThrow(() -> new NoSuchElementException("Task not found for soft softDeleteModule: " + taskId));

    }

    @Override
    public Boolean hardDeleteTask(String courseId, String moduleId, String taskId) {
        return taskRepo
                .findByIdAndModule_IdAndModule_Course_Id(taskId, moduleId, courseId)
                .map(task -> {
                    taskRepo.delete(task);
                    return true;
                })
                .orElseThrow(() -> new NoSuchElementException("Task not found for hard softDeleteModule: " + taskId));
    }
}
