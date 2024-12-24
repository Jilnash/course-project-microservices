
package com.jilnash.homeworkservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "course-access-service",
        url = "http://localhost:8090/api/"
)
public interface CourseAccessClient {

    @GetMapping("v1/course-access")
    Boolean getStudentHasAccess(@RequestParam String studentId, @RequestParam String courseId);
}
