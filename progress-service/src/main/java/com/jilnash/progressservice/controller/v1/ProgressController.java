package com.jilnash.progressservice.controller.v1;

import com.jilnash.progressservice.service.StudentTaskCompleteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{student}/completed")
public class ProgressController {

    private final StudentTaskCompleteService studentTaskCompleteService;

    public ProgressController(StudentTaskCompleteService studentTaskCompleteService) {
        this.studentTaskCompleteService = studentTaskCompleteService;
    }

    @GetMapping
    public List<String> getCompletedTasks(@PathVariable String studentId) {
        return studentTaskCompleteService.getCompletedTaskIds(studentId);
    }

    @PostMapping
    public ResponseEntity<?> completeTask(@PathVariable String studentId, @RequestParam String taskId) {

        return ResponseEntity.ok(
                studentTaskCompleteService.completeTask(studentId, taskId)
        );
    }

    @GetMapping("/{taskId}")
    public Boolean isTaskComplete(@PathVariable String studentId, @PathVariable String taskId) {
        return studentTaskCompleteService.isTaskComplete(studentId, taskId);
    }
}
