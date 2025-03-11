package com.jilnash.fileservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static software.amazon.awssdk.core.sync.RequestBody.fromBytes;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service implements StorageService {

    private final S3Client s3Client;

    private final S3Presigner s3Presigner;

    private static final long EXPIRATION_MINUTES = 120;

    @Override
    public String putFiles(String bucketName, String fileName, List<MultipartFile> file) throws IOException {

        log.info("[SERVICE] Uploading files to bucket");
        log.debug("[SERVICE] Uploading files to bucket: {}, with name: {}", bucketName, fileName);

        if (s3Client.listBuckets().buckets().stream().noneMatch(b -> b.name().equals(bucketName))) {
            s3Client.createBucket(
                    CreateBucketRequest.builder()
                            .bucket(bucketName)
                            .build()
            );
        }

        for (MultipartFile multipartFile : file) {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName + "/" + multipartFile.getOriginalFilename())
                            .build(),
                    fromBytes(multipartFile.getBytes())
            );
        }
        return "";
    }

    @Override
    public byte[] getFile(String bucketName, String fileName) throws IOException {

        log.info("[SERVICE] Fetching file from bucket");
        log.debug("[SERVICE] Fetching file from bucket: {}, with name: {}", bucketName, fileName);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        ResponseInputStream<GetObjectResponse> object = s3Client.getObject(getObjectRequest);

        return object.readAllBytes();
    }

    @Cacheable(value = "task-video-presigned", key = "#bucketName + #keyName")
    public String getPreSignedUrl(String bucketName, String keyName) {

        log.info("[SERVICE] Fetching presigned url");
        log.debug("[SERVICE] Fetching presigned url for file: {}, in bucket: {}", keyName, bucketName);

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(EXPIRATION_MINUTES))
                .getObjectRequest(
                        GetObjectRequest.builder()
                                .bucket(bucketName)
                                .key(keyName)
                                .build()
                )
                .build();

        return s3Presigner.presignGetObject(getObjectPresignRequest).url().toString();
    }
}
