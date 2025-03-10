package com.jilnash.courseservice.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
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
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class FileClient {

    private final String FILE_SERVICE_URL = "http://localhost:8087/api/v1/files";

    @SneakyThrows
    @Async
    public void uploadFiles(String bucket, String fileName, List<MultipartFile> files) {

        log.info("[EXTERNAL] Uploading files to file-service");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("bucket", bucket);
        body.add("fileName", fileName);

        // Convert MultipartFile to ByteArrayResource
        for (MultipartFile file : files) {
            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();  // Ensure the filename is included
                }
            };
            HttpHeaders fileHeaders = new HttpHeaders();
            fileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            HttpEntity<ByteArrayResource> fileEntity = new HttpEntity<>(resource, fileHeaders);
            body.add("files", fileEntity);
        }

        new RestTemplate().postForEntity(FILE_SERVICE_URL, new HttpEntity<>(body, headers), String.class);
    }

    @Async
    public CompletableFuture<String> getPreSignedUrl(String bucket, String fileName) {

        String url = new RestTemplate()
                .getForObject(FILE_SERVICE_URL + "/" + bucket + "/presigned?fileName=" + fileName, String.class);
        return CompletableFuture.completedFuture(url);
    }
}
