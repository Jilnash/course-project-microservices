package com.jilnash.courseservice.controller.v1;

import com.jilnash.courseservice.dto.AppResponse;
import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.dto.task.TaskUpdateDTO;
import com.jilnash.courseservice.service.task.TaskServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/modules/{moduleId}/tasks")
public class TaskController {

    private final TaskServiceImpl taskService;

    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<?> getTasks(@PathVariable String courseId,
                                      @PathVariable String moduleId,
                                      @RequestParam(required = false, defaultValue = "") String name) {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Tasks fetched successfully",
                        taskService.getTasks(courseId, moduleId, name)
                )
        );
    }

    @PutMapping
    public ResponseEntity<?> createTask(@PathVariable String courseId,
                                        @PathVariable String moduleId,
                                        @RequestBody TaskCreateDTO taskDto) {

        taskDto.setCourseId(courseId);
        taskDto.setModuleId(moduleId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task created successfully",
                        taskService.create(taskDto)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable String courseId,
                                     @PathVariable String moduleId,
                                     @PathVariable String id) {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task fetched successfully",
                        taskService.getTask(courseId, moduleId, id)
                )
        );
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable String courseId,
                                        @PathVariable String moduleId,
                                        @PathVariable String id,
                                        @RequestBody TaskUpdateDTO taskDto) {

        taskDto.setId(id);
        taskDto.setCourseId(courseId);
        taskDto.setModuleId(moduleId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task updated successfully",
                        taskService.update(taskDto)
                )
        );
    }

    @GetMapping("/{id}/prerequisites")
    public ResponseEntity<?> getTaskPrerequisites(@PathVariable String courseId,
                                                  @PathVariable String moduleId,
                                                  @PathVariable String id) {

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task prereqs fetched successfully",
                        taskService.getTaskPrerequisites(courseId, moduleId, id)
                )
        );
    }

    @PostMapping("/{id}/prerequisites")
    public ResponseEntity<?> addTaskPrerequisite(@PathVariable String courseId,
                                                 @PathVariable String moduleId,
                                                 @PathVariable String id,
                                                 @RequestBody List<String> prerequisiteIds) {

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task prereqs updated successfully",
                        taskService.addTaskPrerequisite(courseId, moduleId, id, prerequisiteIds)
                )
        );
    }
}
