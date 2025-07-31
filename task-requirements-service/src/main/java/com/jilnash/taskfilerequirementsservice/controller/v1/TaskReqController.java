package com.jilnash.taskfilerequirementsservice.controller.v1;

import com.jilnash.taskfilerequirementsservice.service.TaskFileReqService;
import com.jilnash.taskrequirementsservicedto.dto.FileReqirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskReqController {

    private final TaskFileReqService taskFileReqService;

    @GetMapping("/{taskId}/requirements")
    public List<FileReqirement> getTaskRequirements(@PathVariable String taskId) {
        return taskFileReqService.getTaskRequirements(taskId);
    }

    @PostMapping("/{taskId}/requirements")
    public Boolean setTaskRequirements(@PathVariable String taskId, @RequestBody List<FileReqirement> requirements) {
        return taskFileReqService.setTaskRequirements(taskId, requirements);
    }
}
