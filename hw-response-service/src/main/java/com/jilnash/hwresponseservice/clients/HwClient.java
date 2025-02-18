package com.jilnash.hwresponseservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@Component
@FeignClient(
        name = "homework-service",
        url = "http://localhost:8088"
)
public interface HwClient {

    @GetMapping("/api/v1/homeworks/{id}/task/id")
    String getTaskId(@PathVariable String id);

    @GetMapping("/api/v1/homeworks/{hwId}/student/id")
    String getStudentId(@PathVariable String hwId);

    @Async
    @PutMapping("/api/v1/homeworks/{id}/checked")
    void setChecked(@PathVariable String id);
}
