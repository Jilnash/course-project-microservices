package com.jilnash.fileservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

import static software.amazon.awssdk.core.sync.RequestBody.fromBytes;

@Service
@RequiredArgsConstructor
public class S3Service implements StorageService {

    private final S3Client s3;

    @Override
    public String putFile(String bucketName, String fileName, MultipartFile file) throws IOException {

        s3.createBucket(
                CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build()
        );

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build()
                ,
                fromBytes(file.getBytes())
        );

        return file.getOriginalFilename();
    }

    @Override
    public byte[] getFile(String bucketName, String fileName) throws IOException {

        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .maxKeys(1)
                .build();

        S3Object firstObject = s3.listObjectsV2(listObjectsV2Request).contents().getFirst();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(firstObject.key())
                .build();

        ResponseInputStream<GetObjectResponse> object = s3.getObject(getObjectRequest);

        return object.readAllBytes();
    }
}
