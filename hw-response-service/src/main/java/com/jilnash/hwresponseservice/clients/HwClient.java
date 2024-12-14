package com.jilnash.hwresponseservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(
        name = "homework-service",
        url = "http://localhost:8088"
)
public interface HwClient {

    @GetMapping("/api/v1/homeworks/{id}/task/id")
    String getTaskId(@PathVariable Long id);

    @GetMapping("/api/v1/homeworks/{hwId}/student/id")
    String getStudentId(@PathVariable Long hwId);
}
