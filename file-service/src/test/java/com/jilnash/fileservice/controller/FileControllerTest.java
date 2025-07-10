package com.jilnash.fileservice.controller;

import com.jilnash.fileservice.dto.FileUploadDTO;
import com.jilnash.fileservice.service.MinIOService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MinIOService minIOService;

    @Test
    void testGetFile_Success() throws Exception {
        String bucketName = "test-bucket";
        String fileName = "test-file.txt";
        String fileContent = "This is a test file.";

        when(minIOService.getFile(bucketName, fileName)).thenReturn(fileContent.getBytes());

        mockMvc.perform(get("/api/v1/files/{bucketName}", bucketName)
                        .param("fileName", fileName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(fileContent));
    }

    @Test
    void testGetFile_NotFound() throws Exception {
        String bucketName = "test-bucket";
        String fileName = "non-existent-file.txt";

        when(minIOService.getFile(bucketName, fileName)).thenThrow(new RuntimeException("File not found"));

        mockMvc.perform(get("/api/v1/files/{bucketName}", bucketName)
                        .param("fileName", fileName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"status\":500,\"message\":\"File not found\"}"));
    }

    @Test
    void testGetFile_InvalidRequest() throws Exception {
        String bucketName = "";
        String fileName = "";

        mockMvc.perform(get("/api/v1/files/{bucketName}", bucketName)
                        .param("fileName", fileName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPresignedUrl_Success() throws Exception {
        String bucketName = "test-bucket";
        String fileName = "test-file.txt";
        String presignedUrl = "http://example.com/presigned-url";

        when(minIOService.getPreSignedUrl(bucketName, fileName)).thenReturn(presignedUrl);

        mockMvc.perform(get("/api/v1/files/{bucketName}/presigned", bucketName)
                        .param("fileName", fileName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(presignedUrl));
    }

    @Test
    void testGetPresignedUrl_NotFound() throws Exception {
        String bucketName = "test-bucket";
        String fileName = "non-existent-file.txt";

        when(minIOService.getPreSignedUrl(bucketName, fileName)).thenThrow(new RuntimeException("File not found"));

        mockMvc.perform(get("/api/v1/files/{bucketName}/presigned", bucketName)
                        .param("fileName", fileName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"status\":500,\"message\":\"File not found\"}"));
    }

    @Test
    void testGetPresignedUrl_InvalidRequest() throws Exception {
        String bucketName = "";
        String fileName = "";

        mockMvc.perform(get("/api/v1/files/{bucketName}/presigned", bucketName)
                        .param("fileName", fileName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUploadFiles_Success() throws Exception {
        String bucketName = "test-bucket";
        String fileName = "test-file.txt";

        MockMultipartFile mockFile = new MockMultipartFile(
                "files",
                "test-file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test content".getBytes()
        );

        FileUploadDTO fileUploadDTO = new FileUploadDTO(
                bucketName,
                fileName,
                List.of(mockFile)
        );

        when(minIOService.uploadFiles(bucketName, fileName, fileUploadDTO.files())).thenReturn(true);

        mockMvc.perform(multipart("/api/v1/files")
                        .file(mockFile)
                        .param("bucket", bucketName)
                        .param("fileName", fileName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":200,\"message\":\"File uploaded successfully\",\"data\":true}"));
    }

    @Test
    void testUploadFiles_UploadFailure() throws Exception {
        String bucketName = "test-bucket";
        String fileName = "test-file.txt";

        MockMultipartFile mockFile = new MockMultipartFile(
                "files",
                "test-file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test content".getBytes()
        );

        when(minIOService.uploadFiles(bucketName, fileName, List.of(mockFile)))
                .thenThrow(new RuntimeException("Upload failed"));

        mockMvc.perform(multipart("/api/v1/files")
                        .file(mockFile)
                        .param("bucket", bucketName)
                        .param("fileName", fileName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"status\":500,\"message\":\"Upload failed\"}"));
    }

    @Test
    void testUploadFiles_ValidationFailure() throws Exception {
        String bucketName = "";
        String fileName = "";

        mockMvc.perform(multipart("/api/v1/files")
                        .file("files", "invalid-content".getBytes())
                        .param("bucket", bucketName)
                        .param("fileName", fileName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}