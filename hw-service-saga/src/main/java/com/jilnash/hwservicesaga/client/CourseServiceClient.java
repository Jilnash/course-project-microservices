package com.jilnash.hwservicesaga.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@EnableAsync
@Component
public class CourseServiceClient {

    private final RestTemplate restTemplate;

    public CourseServiceClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<List<String>> getTaskPrerequisites(String taskId) {

        String url = "http://localhost:8086/api/v2/tasks/{id}/prerequisites";

        List<String> result = restTemplate.getForObject(url, List.class, taskId);

        return CompletableFuture.completedFuture(result);
    }

}
