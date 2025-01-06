package com.jilnash.taskfilerequirementsservice.controller.v1;

import com.jilnash.taskfilerequirementsservice.dto.TaskFileReqDTO;
import com.jilnash.taskfilerequirementsservice.service.TaskFileRequirementsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskReqController {

    private final TaskFileRequirementsService taskFileRequirementsService;

    @GetMapping("/{taskId}/requirements")
    public List<TaskFileReqDTO> getTaskRequirements(@PathVariable String taskId) {
        return taskFileRequirementsService.getTaskRequirements(taskId);
    }

    @PostMapping("/{taskId}/requirements")
    public Boolean setTaskRequirements(@PathVariable String taskId, @RequestBody List<TaskFileReqDTO> requirements) {
        return taskFileRequirementsService.setTaskRequirements(taskId, requirements);
    }
}
