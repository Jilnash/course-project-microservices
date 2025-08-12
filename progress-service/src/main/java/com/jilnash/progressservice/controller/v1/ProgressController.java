package com.jilnash.progressservice.controller.v1;

import com.jilnash.progressservice.service.StudentProgressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{studentId}/completed-tasks")
public class ProgressController {

    private final StudentProgressService studentProgressService;

    public ProgressController(StudentProgressService studentProgressService) {
        this.studentProgressService = studentProgressService;
    }

    @GetMapping
    public List<String> getCompletedTasks(@PathVariable String studentId) {
        return studentProgressService.getStudentCompletedTaskIds(studentId);
    }

    @GetMapping("/check")
    public Boolean checkIfCompletedTasks(@PathVariable String studentId, @RequestBody List<String> taskIds) {
        return studentProgressService.getIfStudentCompletedTasks(studentId, taskIds);
    }
}
