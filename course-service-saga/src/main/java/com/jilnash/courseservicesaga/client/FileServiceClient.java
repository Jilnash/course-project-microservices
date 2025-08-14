package com.jilnash.courseservicesaga.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@EnableAsync
public class FileServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    //    @Value("{file.service.url}")
    private final String fileServiceUrl = "http://localhost:8087/api/v1/files";

    @Async
    public void uploadFileAsync(String taskId, MultipartFile videoFile) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("bucket", "course-project-tasks");
        builder.part("fileName", "task-" + taskId);
        builder.part("files", videoFile.getResource());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<?> entity = new HttpEntity<>(builder.build(), headers);

        restTemplate.exchange(fileServiceUrl, HttpMethod.POST, entity, String.class);
    }
}
