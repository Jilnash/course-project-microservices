package com.jilnash.fileservice.service;

import io.minio.*;
import io.minio.http.Method;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MinIOServiceTest {

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private MinIOService minIOService;

    @Test
    void getPreSignedUrl_ShouldReturnPresignedUrl_WhenParametersAreValid() throws Exception {
        // Arrange
        String bucket = "test-bucket";
        String fileName = "test-file.txt";

        String expectedUrl = "https://example.com/test-bucket/test-file.txt";
        when(minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucket)
                        .object(fileName)
                        .method(Method.GET)
                        .expiry(60 * 60 * 2)
                        .build()))
                .thenReturn(expectedUrl);

        // Act
        String presignedUrl = minIOService.getPreSignedUrl(bucket, fileName);

        // Assert
        assertEquals(expectedUrl, presignedUrl);

        verify(minioClient, times(1)).getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class));
    }

    @Test
    void getPreSignedUrl_ShouldThrowException_WhenMinioClientFails() throws Exception {
        // Arrange
        String bucket = "test-bucket";
        String fileName = "non-existent-file.txt";

        when(minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucket)
                        .object(fileName)
                        .method(Method.GET)
                        .expiry(60 * 60 * 2)
                        .build()))
                .thenThrow(new RuntimeException("MinIO operation failed"));

        // Act & Assert
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () ->
                minIOService.getPreSignedUrl(bucket, fileName)
        );

        assertEquals("MinIO operation failed", exception.getMessage());

        verify(minioClient, times(1)).getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class));
    }

    @Test
    void getFile_ShouldReturnFileData_WhenFileExists() throws Exception {
        // Arrange
        String bucket = "test-bucket";
        String fileName = "test-file.txt";
        byte[] expectedData = "File content".getBytes();

        GetObjectResponse mockResponse = mock(GetObjectResponse.class);
        when(mockResponse.readAllBytes()).thenReturn(expectedData);
        when(minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(fileName).build()))
                .thenReturn(mockResponse);

        // Act
        byte[] fileData = minIOService.getFile(bucket, fileName);

        // Assert
        assertEquals(new String(expectedData), new String(fileData));
        verify(minioClient, times(1)).getObject(any(GetObjectArgs.class));
    }

    @Test
    void getFile_ShouldThrowException_WhenFileDoesNotExist() throws Exception {
        // Arrange
        String bucket = "test-bucket";
        String fileName = "non-existent-file.txt";

        when(minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(fileName).build()))
                .thenThrow(new RuntimeException("File not found"));

        // Act & Assert
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () ->
                minIOService.getFile(bucket, fileName)
        );

        assertEquals("File not found", exception.getMessage());
        verify(minioClient, times(1)).getObject(any(GetObjectArgs.class));
    }

    @Test
    void uploadFiles_ShouldUploadFiles_WhenAllParametersAreValid() throws Exception {
        // Arrange
        String bucket = "test-bucket";
        String filename = "folder";
        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);
        when(file1.getOriginalFilename()).thenReturn("file1.txt");
        when(file1.getInputStream()).thenReturn(mock(java.io.InputStream.class));
        when(file1.getSize()).thenReturn(100L);
        when(file2.getOriginalFilename()).thenReturn("file2.txt");
        when(file2.getInputStream()).thenReturn(mock(java.io.InputStream.class));
        when(file2.getSize()).thenReturn(200L);
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);

        // Act
        Boolean result = minIOService.uploadFiles(bucket, filename, List.of(file1, file2));

        // Assert
        assertEquals(true, result);
        verify(minioClient, times(1)).bucketExists(any(BucketExistsArgs.class));
        verify(minioClient, times(2)).putObject(any(PutObjectArgs.class));
    }

    @Test
    void uploadFiles_ShouldCreateBucket_WhenBucketDoesNotExist() throws Exception {
        // Arrange
        String bucket = "new-bucket";
        String filename = "folder";
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("file1.txt");
        when(file.getInputStream()).thenReturn(mock(java.io.InputStream.class));
        when(file.getSize()).thenReturn(100L);
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(false);

        // Act
        Boolean result = minIOService.uploadFiles(bucket, filename, List.of(file));

        // Assert
        assertEquals(true, result);
        verify(minioClient, times(1)).bucketExists(any(BucketExistsArgs.class));
        verify(minioClient, times(1)).makeBucket(any(MakeBucketArgs.class));
        verify(minioClient, times(1)).putObject(any(PutObjectArgs.class));
    }

    @Test
    void uploadFiles_ShouldThrowException_WhenUploadFails() throws Exception {
        // Arrange
        String bucket = "test-bucket";
        String filename = "folder";
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("file1.txt");
        when(file.getInputStream()).thenReturn(mock(java.io.InputStream.class));
        when(file.getSize()).thenReturn(100L);
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);
        doThrow(new RuntimeException("Upload failed"))
                .when(minioClient).putObject(any(PutObjectArgs.class));

        // Act & Assert
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () ->
                minIOService.uploadFiles(bucket, filename, List.of(file))
        );

        assertEquals("Upload failed", exception.getMessage());
        verify(minioClient, times(1)).bucketExists(any(BucketExistsArgs.class));
        verify(minioClient, times(1)).putObject(any(PutObjectArgs.class));
    }
}