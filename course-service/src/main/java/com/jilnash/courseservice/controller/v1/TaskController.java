package com.jilnash.courseservice.controller.v1;

import com.jilnash.courseservice.dto.AppResponse;
import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.dto.task.TaskUpdateDTO;
import com.jilnash.courseservice.mapper.TaskMapper;
import com.jilnash.courseservice.service.ModuleService;
import com.jilnash.courseservice.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/course/{courseId}/module/{moduleId}/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ModuleService moduleService;

    @GetMapping
    public ResponseEntity<?> getTasks(@PathVariable Long courseId,
                                      @PathVariable Long moduleId,
                                      @RequestParam String title) {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Tasks fetched successfully",
                        taskService.getTasks(title, courseId, moduleId)
                )
        );
    }

    @PutMapping
    public ResponseEntity<?> createTask(@PathVariable Long courseId,
                                        @PathVariable Long moduleId,
                                        @Validated @RequestBody TaskCreateDTO taskDTO) {

        //checking if module exists, then setting module id
        taskDTO.setModuleId(moduleService.getModule(moduleId, courseId).getId());

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task created successfully",
                        taskService.save(taskMapper.toEntity(taskDTO))
                )
        );
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTask(@PathVariable Long courseId,
                                     @PathVariable Long moduleId,
                                     @PathVariable Long taskId) {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task fetched successfully",
                        taskService.getTask(taskId, moduleId, courseId)
                )
        );
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long courseId,
                                        @PathVariable Long moduleId,
                                        @PathVariable Long taskId,
                                        @Validated @RequestBody TaskUpdateDTO taskDTO) {

        //checking if task exists, then setting id
        taskDTO.setId(taskService.getTask(taskId, moduleId, courseId).getId());

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task updated successfully",
                        taskService.save(taskMapper.toEntity(taskDTO))
                )
        );
    }
}
