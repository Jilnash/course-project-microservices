package com.jilnash.fileservice.service;

import io.minio.*;
import io.minio.messages.Item;
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
    public void rollbackFileUpdate(String binBucket, String workingBucket, String fileName) throws Exception {

        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(workingBucket).build()))
            throw new RuntimeException("Bucket does not exist: " + workingBucket);

        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(workingBucket)
                            .prefix(fileName)
                            .recursive(true)
                            .build()
            );

            for (Result<Item> result : results) {
                Item item = result.get();
                String objectName = item.objectName();

                // 2. Copy to target bucket
                minioClient.copyObject(
                        CopyObjectArgs.builder()
                                .bucket(binBucket)
                                .object(objectName)
                                .source(CopySource.builder()
                                        .bucket(workingBucket)
                                        .object(objectName)
                                        .build())
                                .build()
                );

                // 3. Delete from source bucket
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(workingBucket)
                                .object(objectName)
                                .build()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to rollback file update " + fileName, e);
        }

    }
}
