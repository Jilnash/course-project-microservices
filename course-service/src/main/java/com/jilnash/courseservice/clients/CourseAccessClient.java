package com.jilnash.courseservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "course-access-service",
        url = "http://localhost:8090/api/v1/course-access"
)
public interface CourseAccessClient {

    @GetMapping
    Boolean checkAccess(@RequestParam String courseId, @RequestParam String studentId);
}
