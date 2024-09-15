package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.dto.task.TaskUpdateDTO;
import com.jilnash.courseservice.mapper.TaskMapper;
import com.jilnash.courseservice.model.Task;
import com.jilnash.courseservice.repo.TaskRepo;
import com.jilnash.courseservice.service.module.ModuleServiceImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Task> getTasks(String courseId, String moduleId, String title) {
        return taskRepo.findAllByTitleStartingWithAndModule_IdAndModule_Course_Id(title, moduleId, courseId);
    }

    @Override
    public Task getTask(String courseId, String moduleId, String id) {
        return taskRepo.findByIdAndModule_IdAndModule_Course_Id(id, moduleId, courseId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
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
        return getTask(courseId, moduleId, taskId).getTasks();
    }

    public List<Task> addTaskPrerequisite(String courseId, String moduleId, String taskId,
                                          List<String> prerequisiteId) {
        Task task = getTask(courseId, moduleId, taskId);

        List<Task> prerequisites = taskRepo.findAllByIdIn(prerequisiteId);

        task.getTasks().clear();
        task.getTasks().addAll(prerequisites);

        return taskRepo.save(task).getTasks();
    }
}
