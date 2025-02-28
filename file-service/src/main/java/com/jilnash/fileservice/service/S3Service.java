package com.jilnash.fileservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static software.amazon.awssdk.core.sync.RequestBody.fromBytes;

@Service
@RequiredArgsConstructor
public class S3Service implements StorageService {

    private final S3Client s3;

    private static final long EXPIRATION_MINUTES = 120;

    @Value("${aws.access-key}")
    private static String ACCESS_KEY;

    @Value("${aws.secret-key}")
    private static String SECRET_KEY;

    @Override
    public String putFiles(String bucketName, String fileName, List<MultipartFile> file) throws IOException {

        if (s3.listBuckets().buckets().stream().noneMatch(b -> b.name().equals(bucketName))) {
            s3.createBucket(
                    CreateBucketRequest.builder()
                            .bucket(bucketName)
                            .build()
            );
        }

        for (MultipartFile multipartFile : file) {
            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName + "\\" + multipartFile.getOriginalFilename())
                            .build(),
                    fromBytes(multipartFile.getBytes())
            );
        }
        return "";
    }

    @Override
    public byte[] getFile(String bucketName, String fileName) throws IOException {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        ResponseInputStream<GetObjectResponse> object = s3.getObject(getObjectRequest);

        return object.readAllBytes();
    }

    public String getPreSignedUrl(String bucketName, String keyName) {

        S3Presigner presigner = S3Presigner.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)))
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(EXPIRATION_MINUTES))
                .getObjectRequest(
                        GetObjectRequest.builder()
                                .bucket(bucketName)
                                .key(keyName)
                                .build()
                )
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);

        return presignedGetObjectRequest.url().toString();
    }
}
