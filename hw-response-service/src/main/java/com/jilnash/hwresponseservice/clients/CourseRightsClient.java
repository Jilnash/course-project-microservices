package com.jilnash.hwresponseservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(
        name = "course-rights-service",
        url = "http://localhost:8091/api"
)
public interface CourseRightsClient {

    @GetMapping("/v1/courses/{courseId}/teachers/{teacherId}/rights/has")
    Boolean hasRights(@PathVariable String courseId,
                      @PathVariable String teacherId,
                      @RequestParam List<String> rights);
}

