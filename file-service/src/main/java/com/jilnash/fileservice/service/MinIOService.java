package com.jilnash.fileservice.service;

import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MinIOService implements StorageService {

    private final MinioClient minioClient;

    @Override
    public Boolean putFiles(String bucket, String filename, List<MultipartFile> fileContent) throws Exception {

        // Check if the bucket exists
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }

        // Upload each file to the bucket, nested under the filename prefix
        for (MultipartFile file : fileContent) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(filename + "/" + file.getOriginalFilename())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build()
            );
        }

        return true;
    }

    @Override
    public byte[] getFile(String bucket, String fileName) throws Exception {
        return minioClient
                .getObject(GetObjectArgs.builder().bucket(bucket).object(fileName).build())
                .readAllBytes();
    }

    @Override
    @Cacheable(value = "file", key = "#bucket + #fileName")
    public String getPreSignedUrl(String bucket, String fileName) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucket)
                        .object(fileName)
                        .method(Method.GET)
                        .expiry(60 * 60 * 2) // 2 hours
                        .build()
        );
    }
}
