package com.jilnash.progressservice.controller.v1;

import com.jilnash.progressservice.service.TaskCompleteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskCompleteService taskCompleteService;

    public TaskController(TaskCompleteService taskCompleteService) {
        this.taskCompleteService = taskCompleteService;
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestParam String newTaskId,
                                        @RequestParam List<String> completedTaskIds) {
        return ResponseEntity.ok(taskCompleteService.insertTask(newTaskId, completedTaskIds));
    }

    @DeleteMapping("/soft")
    public ResponseEntity<?> softDeleteTask(@RequestParam List<String> taskIds) {
        return ResponseEntity.ok(taskCompleteService.softDeleteTasks(taskIds));
    }

    @DeleteMapping("/hard")
    public ResponseEntity<?> hardDeleteTask(@RequestParam List<String> taskIds) {
        return ResponseEntity.ok(taskCompleteService.hardDeleteTasks(taskIds));
    }
}
