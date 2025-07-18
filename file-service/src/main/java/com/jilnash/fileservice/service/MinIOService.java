package com.jilnash.fileservice.service;

import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service implementation for interacting with a MinIO storage system. Provides methods
 * to upload multiple files, retrieve files, and generate pre-signed URLs for accessing files.
 * This service uses {@link MinioClient} to perform operations on MinIO buckets and objects.
 */
@Service
@RequiredArgsConstructor
public class MinIOService implements StorageService {

    private final MinioClient minioClient;

    /**
     * Uploads multiple files to the specified bucket in the MinIO storage. If the
     * specified bucket does not exist, it will be created. Files are uploaded and
     * stored under a directory named by the provided filename prefix.
     *
     * @param bucket      the name of the bucket where the files will be stored
     * @param filename    the prefix/directory under which the files will be stored
     * @param fileContent a list of {@link MultipartFile} objects containing the content of the files to be uploaded
     * @return a {@code Boolean} indicating whether the files were successfully uploaded
     * @throws Exception if an error occurs during the operation, such as issues with bucket creation or file upload
     */
    @Override
    public Boolean uploadFiles(String bucket, String filename, List<MultipartFile> fileContent) throws Exception {

        // Check if the bucket exists
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build()))
            throw new RuntimeException("Bucket does not exist: " + bucket);

        // Upload each file to the bucket, nested under the filename prefix
        fileContent.parallelStream().forEach(file -> {
            try {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucket)
                                .object(filename + "/" + file.getOriginalFilename())
                                .stream(file.getInputStream(), file.getSize(), -1)
                                .build()
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename() + " to bucket: " + bucket, e);
            }
        });

        return true;
    }

    /**
     * Retrieves a file from the specified bucket in the MinIO storage.
     *
     * @param bucket   the name of the bucket from which the file will be retrieved
     * @param fileName the name of the file to be fetched
     * @return a byte array containing the contents of the retrieved file
     * @throws Exception if an error occurs during the file retrieval process, such as issues with the bucket or file access
     */
    @Override
    public byte[] getFile(String bucket, String fileName) throws Exception {
        return minioClient
                .getObject(GetObjectArgs.builder().bucket(bucket).object(fileName).build())
                .readAllBytes();
    }

    /**
     * Generates a pre-signed URL for fetching a file from the specified bucket in the MinIO storage.
     * The URL is valid for 2 hours and allows file access via HTTP GET method.
     *
     * @param bucket   the name of the bucket containing the file
     * @param fileName the name of the file for which the pre-signed URL is generated
     * @return a pre-signed URL as a {@code String} that provides temporary access to the specified file
     * @throws Exception if an error occurs during the generation of the pre-signed URL
     */
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

    /**
     * Moves a file from the specified bucket to a "deleted" bucket, effectively performing a "soft delete."
     * If the "deleted" bucket does not exist, it is created. The file is first copied to the "deleted" bucket
     * and then removed from the original bucket.
     *
     * @param bucketName the name of the bucket containing the file to be soft deleted
     * @param fileName   the name of the file to be soft deleted
     * @throws RuntimeException if an error occurs during the operation, such as issues with bucket creation, file copying, or file removal
     */
    @Override
    public void softDeleteFile(String bucketName, String fileName) {
        try {
            String deletedBucketName = bucketName + "-deleted";

            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(deletedBucketName).build()))
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(deletedBucketName).build());

            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .source(CopySource.builder().bucket(bucketName).object(fileName).build())
                            .bucket(deletedBucketName)
                            .object(fileName)
                            .build()
            );

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to soft delete file: " + fileName + " from bucket: " + bucketName, e);
        }
    }
}
