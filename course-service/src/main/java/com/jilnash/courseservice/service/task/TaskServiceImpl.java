package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.dto.task.TaskResponseDTO;
import com.jilnash.courseservice.dto.task.TaskUpdateDTO;
import com.jilnash.courseservice.mapper.TaskMapper;
import com.jilnash.courseservice.model.Task;
import com.jilnash.courseservice.repo.TaskRepo;
import com.jilnash.courseservice.service.module.ModuleServiceImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;

    private final TaskMapper taskMapper;

    private final ModuleServiceImpl moduleService;

    public TaskServiceImpl(TaskRepo taskRepo, TaskMapper taskMapper, ModuleServiceImpl moduleService) {
        this.taskRepo = taskRepo;
        this.taskMapper = taskMapper;
        this.moduleService = moduleService;
    }

    @Override
    public List<TaskResponseDTO> getTasks(String courseId, String moduleId, String title) {
        return taskRepo
                .findAllByTitleStartingWithAndModule_IdAndModule_Course_Id(title, moduleId, courseId)
                .stream()
                .map(taskMapper::toTaskResponse)
                .toList();
    }

    @Override
    public TaskResponseDTO getTask(String courseId, String moduleId, String id) {
        return taskMapper.toTaskResponse(
                taskRepo.getTaskData(id, moduleId, courseId)
                        .orElseThrow(() -> new RuntimeException("Task not found"))
        );
    }

    @Override
    public Task create(TaskCreateDTO task) {

        task.setModule(moduleService.getModuleByCourse(task.getCourseId(), task.getModuleId()));

        return taskRepo.save(taskMapper.toNode(task));
    }

    @Override
    public Task update(TaskUpdateDTO task) {

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

        Task task = taskRepo
                .findByIdAndModule_IdAndModule_Course_Id(taskId, moduleId, courseId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        task.getTasks().clear();
        task.getTasks().addAll(taskRepo.findAllByIdIn(prerequisiteId));

        return taskRepo.save(task).getTasks().stream().map(taskMapper::toTaskResponse).toList();
    }
}
