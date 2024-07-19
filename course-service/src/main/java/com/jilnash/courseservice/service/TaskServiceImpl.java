package com.jilnash.courseservice.service;

import com.jilnash.courseservice.model.Task;
import com.jilnash.courseservice.repo.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepo taskRepo;

    @Override
    public List<Task> getTasks(String title, Long courseId, Long moduleId) {

        if (title == null)
            return taskRepo.findAllByModule_IdAndModule_Course_id(moduleId, courseId);

        return taskRepo.findAllByTitleAndModuleIdAndModuleCourseId(title, moduleId, courseId);
    }

    @Override
    public Task getTask(Long id, Long moduleId, Long courseId) {
        return taskRepo
                .findByIdAndModuleIdAndModuleCourseId(id, moduleId, courseId)
                .orElseThrow(() -> new NoSuchElementException(
                                "Task not found with id: " + id +
                                        " in module: " + moduleId +
                                        " in course: " + courseId
                        )
                );
    }

    @Override
    public Task save(Task task) {
        return taskRepo.save(task);
    }
}
