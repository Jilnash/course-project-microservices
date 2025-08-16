package com.jilnash.courseservice.controller.v2;

import com.jilnash.courseservice.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v2/tasks")
@RequiredArgsConstructor
public class TaskControllerV2 {

    private final TaskService taskService;

    @GetMapping("/{taskId}/course")
    public String getTaskCourseId(@PathVariable String taskId) {

        return taskService.getTaskCourseId(taskId);
    }

    @GetMapping("/{taskId}/module")
    public String getTaskModuleId(@PathVariable String taskId) {

        return taskService.getTaskModuleId(taskId);
    }

    @GetMapping("/{taskId}/title")
    public String getTaskTitle(@PathVariable String taskId) {

        return taskService.getTaskTitle(taskId);
    }

    @GetMapping("/{taskId}/description")
    public String getTaskDescription(@PathVariable String taskId) {

        return taskService.getTaskDescription(taskId);
    }

    @GetMapping("/{taskId}/videoFileName")
    public String getTaskVideoFileName(@PathVariable String taskId) {

        return taskService.getTaskVideoFileName(taskId);
    }

    @GetMapping("/{taskId}/isPublic")
    public Boolean isTaskPublic(@PathVariable String taskId) {

        return taskService.getTaskIsPublic(taskId);
    }

    @GetMapping("/{taskId}/hwPostingInterval")
    public Integer getTaskHwPostingInterval(@PathVariable String taskId) {

        return taskService.getTaskHwPostingInterval(taskId);
    }

    @GetMapping("/{taskId}/prerequisites")
    private Set<String> getTaskPreRequisites(@PathVariable String taskId) {

        return taskService.getTaskPrerequisites(taskId);
    }

    @GetMapping("/{taskId}/successors")
    private Set<String> getTaskSuccessors(@PathVariable String taskId) {

        return taskService.getTaskSuccessors(taskId);
    }
}
