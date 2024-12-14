package com.jilnash.courseservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "progress-service",
        url = "http://localhost:8092/api"
)
public interface ProgressClient {

    @PostMapping("/v1/tasks")
    void insertTaskToProgression(@RequestParam String newTaskId, @RequestParam List<String> completedTaskIds);
}
