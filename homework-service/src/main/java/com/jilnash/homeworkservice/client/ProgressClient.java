package com.jilnash.homeworkservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "progress-service",
        url = "http://localhost:8092/api/"
)
public interface ProgressClient {

    @GetMapping("v1/{studentId}/completed/{taskId}")
    Boolean isTaskCompletedByStudent(@PathVariable String studentId, @PathVariable String taskId);
}
