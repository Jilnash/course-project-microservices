package com.jilnash.hwresponseservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "course-service",
        url = "http://localhost:8086/api/"
)
public interface CourseClient {

    @GetMapping("v2/tasks/{taskId}/course")
    String getTaskCourseId(String taskId);
}
