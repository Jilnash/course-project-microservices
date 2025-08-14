package com.jilnash.fileservice.service;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MinIOServiceRollback implements StorageServiceRollback {

    private final MinioClient minioClient;

    @Override
    public void rollbackFileUpload(String bucket, List<String> fileNames) throws Exception {

        if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            fileNames.parallelStream().forEach(fileName -> {
                        try {
                            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(fileName).build());
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to rollback file upload for " +
                                    fileName + " in bucket " + bucket, e);
                        }
                    }
            );
        }
    }

    @Override
    public void rollbackFileDeletion(String bucket, String fileName) {
        try {
            if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket + "-deleted").build())) {
                minioClient.copyObject(
                        CopyObjectArgs.builder()
                                .source(CopySource.builder().bucket(bucket + "-deleted").object(fileName).build())
                                .bucket(bucket)
                                .object(fileName)
                                .build()
                );
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket + "-deleted")
                        .object(fileName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to rollback file deletion for " +
                    fileName + " from deleted bucket to " + bucket, e);
        }

    }

    @Override
    public void rollbackFileUpdate(String oldBucket, String oldFileName,
                                   String newBucket, String newFileName) throws Exception {

        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(newBucket).build()))
            throw new RuntimeException("Bucket does not exist: " + newBucket);

        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(newBucket).object(newFileName).build());
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(oldBucket)
                            .object(oldFileName)
                            .stream(minioClient.getObject(
                                    GetObjectArgs.builder().bucket(oldBucket).object(oldFileName).build()
                            ), -1, -1)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to rollback file update from " + newFileName + " to " + oldFileName, e);
        }

    }
}
