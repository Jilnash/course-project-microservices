package com.jilnash.progressservice.controller.v1;

import com.jilnash.progressservice.service.StudentTaskCompleteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final StudentTaskCompleteService studentTaskCompleteService;

    public TaskController(StudentTaskCompleteService studentTaskCompleteService) {
        this.studentTaskCompleteService = studentTaskCompleteService;
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestParam String newTaskId,
                                        @RequestParam List<String> completedTaskIds) {
        return ResponseEntity.ok(studentTaskCompleteService.addTask(newTaskId, completedTaskIds));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTask(@RequestParam List<String> taskIds) {
        return ResponseEntity.ok(studentTaskCompleteService.deleteTasks(taskIds));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> substituteTask(@PathVariable String taskId, @RequestParam List<String> taskIds) {
        return ResponseEntity.ok(studentTaskCompleteService.substituteTask(taskId, taskIds));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable String taskId) {
        return ResponseEntity.ok(studentTaskCompleteService.deleteTask(taskId));
    }
}
