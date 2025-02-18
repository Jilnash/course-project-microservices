package com.jilnash.courseservice.controller.v2;

import com.jilnash.courseservice.service.task.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/tasks")
@RequiredArgsConstructor
public class TaskControllerV2 {

    private final TaskServiceImpl taskService;

    @GetMapping("/{taskId}/course")
    public String getTaskCourseId(@PathVariable String taskId) {

        return taskService.getTaskCourseId(taskId);
    }

    @GetMapping("/{taskId}/prerequisites")
    private List<String> getTaskPreRequisites(@PathVariable String taskId) {

        return taskService.getTaskPrerequisites(taskId);
    }
}
