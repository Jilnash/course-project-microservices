package com.jilnash.hwresponseservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "progress-service",
        url = "http://localhost:8092/api/"
)
public interface ProgressClient {

    @PostMapping("v1/{studentId}/completed")
    void addTaskToStudentProgress(@PathVariable String studentId, @RequestParam String taskId);
}
