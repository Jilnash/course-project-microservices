package com.jilnash.homeworkservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "course-service",
        url = "http://localhost:8086/api/"
)
public interface CourseClient {

    @GetMapping("/v2/tasks/{taskId}/course")
    String getTaskCourseId(@PathVariable String taskId);

    @GetMapping("/v2/tasks/{taskId}/file-requirements")
    List<String> getTaskRequirements(@PathVariable String taskId);
}
