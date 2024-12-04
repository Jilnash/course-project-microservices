package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.dto.task.*;
import com.jilnash.courseservice.mapper.TaskMapper;
import com.jilnash.courseservice.model.Task;
import com.jilnash.courseservice.repo.TaskRepo;
import com.jilnash.courseservice.service.course.CourseServiceImpl;
import com.jilnash.courseservice.service.module.ModuleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;

    private final ModuleServiceImpl moduleService;

    private final CourseServiceImpl courseService;

    @Override
    @Cacheable(value = "taskLists", key = "#moduleId")
    public List<TaskResponseDTO> getTasks(String courseId, String moduleId, String title) {

        courseService.validateStudentCourseAccess(courseId, "1");

        return taskRepo
                .findAllByTitleStartingWithAndModule_IdAndModule_Course_Id(title, moduleId, courseId)
                .stream()
                .map(TaskMapper::toTaskResponse)
                .toList();
    }

    @Override
    @Cacheable(value = "tasks", key = "#id")
    public TaskResponseDTO getTask(String courseId, String moduleId, String id) {

        courseService.validateStudentCourseAccess(courseId, "1");

        return TaskMapper.toTaskResponse(
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
    public Boolean create(TaskCreateDTO task) {

        courseService.validateTeacherCourseRights(task.getCourseId(), "1", List.of("add"));
        moduleService.validateModuleExists(task.getModuleId(), task.getCourseId());

        Set<String> prereqsAndSuccessorIds =
                mergePrerequisitesAndSuccessors(task.getPrerequisiteTasksIds(), task.getSuccessorTasksIds());

        if (prereqsAndSuccessorIds.isEmpty())
            return createFirstTaskInModule(task);

        validatePrerequisitesAndSuccessorsDisjoint(task.getPrerequisiteTasksIds(), task.getSuccessorTasksIds());

        moduleService.validateModuleContainsTasks(task.getModuleId(), prereqsAndSuccessorIds);

//        taskRepo.deleteTaskRelationshipsByTaskIdPairs(task.getRemoveRelationshipIds());

        task.setTaskId(UUID.randomUUID().toString());
        taskRepo.createTaskWithRelationships(task);

        return true;
    }

    public Boolean createFirstTaskInModule(TaskCreateDTO taskCreateDTO) {

        // throw exception if module already contains tasks
        if (moduleService.hasAtLeastOneTask(taskCreateDTO.getModuleId()))
            throw new RuntimeException("Task should be linked with other tasks");

        taskRepo.save(TaskMapper.toNode(taskCreateDTO));

        return true;
    }

    @Override
    public Task update(TaskUpdateDTO task) {

        courseService.validateTeacherCourseRights(task.getCourseId(), "1", List.of("edit"));

        if (!taskRepo.existsByIdAndModuleIdAndModule_CourseId(task.getId(), task.getModuleId(), task.getCourseId()))
            throw new RuntimeException("Task not found");

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

        courseService.validateTeacherCourseRights(courseId, "1", List.of("edit"));

        Task task = taskRepo
                .findByIdAndModule_IdAndModule_Course_Id(taskId, moduleId, courseId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        task.getTasks().clear();
        task.getTasks().addAll(taskRepo.findAllByIdIn(prerequisiteId));

        return taskRepo.save(task).getTasks().stream().map(TaskMapper::toTaskResponse).toList();
    }

    public String getTaskCourseId(String taskId) {
        return taskRepo
                .getTaskCourseId(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
    }

    public void validatePrerequisitesAndSuccessorsDisjoint(Set<String> prereqs, Set<String> successors) {
        if (!Collections.disjoint(prereqs, successors))
            throw new NoSuchElementException("Pre-requisites and successor tasks should be distinct");
    }

    public Set<String> mergePrerequisitesAndSuccessors(Set<String> prereqs, Set<String> successors) {
        return Stream.concat(prereqs.stream(), successors.stream()).collect(Collectors.toSet());
    }
}
