package com.jilnash.courseservice.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class FileClient {

    @Async
    public void uploadFiles(String bucket, String fileName, List<MultipartFile> files) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("bucket", bucket);
        body.add("fileName", fileName);
        files.forEach(file -> body.add("files", new HttpEntity<>(file.getResource())));

        new RestTemplate().postForEntity("http://localhost:8087/api/v1/files",
                new HttpEntity<>(body, headers), String.class);
    }
}
