package com.jilnash.homeworkservice.controller.v1;

import com.jilnash.homeworkservice.dto.HomeworkCreateDTO;
import com.jilnash.homeworkservice.dto.HomeworkResponseDTO;
import com.jilnash.homeworkservice.mapper.HomeworkMapper;
import com.jilnash.homeworkservice.model.Homework;
import com.jilnash.homeworkservice.service.HomeworkServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = HomeworkController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class HomeworkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HomeworkServiceImpl homeworkService;

    @MockBean
    private HomeworkMapper homeworkMapper;

    @Test
    void testGetHomeworksWithoutFilters() throws Exception {
        when(homeworkService.getHomeworks(isNull(), isNull(), isNull(), isNull()))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/homeworks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Homeworks fetched successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetHomeworksWithTaskIdFilter() throws Exception {
        when(homeworkService.getHomeworks(eq("task123"), isNull(), isNull(), isNull()))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/homeworks")
                        .param("taskId", "task123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Homeworks fetched successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetHomeworksWithStudentIdFilter() throws Exception {
        when(homeworkService.getHomeworks(isNull(), eq("student123"), isNull(), isNull()))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/homeworks")
                        .param("studentId", "student123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Homeworks fetched successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetHomeworksWithCheckedFilter() throws Exception {
        when(homeworkService.getHomeworks(isNull(), isNull(), eq(true), isNull()))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/homeworks")
                        .param("checked", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Homeworks fetched successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetHomeworksWithCreatedAfterFilter() throws Exception {
        Date createdAfter = Date.valueOf("2023-01-01");
        when(homeworkService.getHomeworks(isNull(), isNull(), isNull(), eq(createdAfter)))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/homeworks")
                        .param("createdAfter", "2023-01-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Homeworks fetched successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }


    @Test
    void testCreateHomeworkSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile("files", "test.txt", "text/plain", "dummy content".getBytes());
        HomeworkCreateDTO homeworkDTO = new HomeworkCreateDTO();
        Homework homeworkEntity = new Homework(); // Replace with actual entity

        when(homeworkMapper.toEntity(homeworkDTO)).thenReturn(homeworkEntity);
        when(homeworkService.saveHomework(homeworkEntity)).thenReturn(true);

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/v1/homeworks")
                        .file(file)
                        .param("taskId", "task123")
                        .header("X-User-Sub", "student123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Homework created successfully"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void testCreateHomeworkFileValidationFailure() throws Exception {

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/v1/homeworks")
                        .param("taskId", "taskId")
                        .header("X-User-Sub", "student123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testCreateHomeworkTaskIdValidationFailure() throws Exception {

        MockMultipartFile file =
                new MockMultipartFile("files", "test.txt", "text/plain", new byte[0]);

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/v1/homeworks")
                        .file(file)
                        .param("taskId", "") // Missing taskId
                        .header("X-User-Sub", "student123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());
    }


    @Test
    void testGetHomeworkSuccess() throws Exception {
        UUID homeworkId = UUID.randomUUID();
        when(homeworkService.getHomeworkDTO(eq(homeworkId)))
                .thenReturn(new HomeworkResponseDTO()); // Replace with actual DTO object

        mockMvc.perform(get("/api/v1/homeworks/{id}", homeworkId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Homework fetched successfully"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void testGetHomeworkNotFound() throws Exception {
        UUID homeworkId = UUID.randomUUID();
        when(homeworkService.getHomeworkDTO(eq(homeworkId)))
                .thenThrow(new EntityNotFoundException("Homework not found"));

        mockMvc.perform(get("/api/v1/homeworks/{id}", homeworkId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Homework not found"));
    }


    @Test
    void testGetFileSuccess() throws Exception {
        UUID homeworkId = UUID.randomUUID();
        String fileName = "example.pdf";
        String fileUrl = "https://example.com/files/example.pdf";

        when(homeworkService.getHwFileURL(eq(homeworkId), eq(fileName)))
                .thenReturn(fileUrl);

        mockMvc.perform(get("/api/v1/homeworks/{id}/files/{fileName}", homeworkId.toString(), fileName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("File fetched successfully"))
                .andExpect(jsonPath("$.data").value(fileUrl));
    }

    @Test
    void testGetFileNotFound() throws Exception {
        UUID homeworkId = UUID.randomUUID();
        String fileName = "nonexistent.pdf";

        when(homeworkService.getHwFileURL(eq(homeworkId), eq(fileName)))
                .thenThrow(new RestClientException("File not found"));

        mockMvc.perform(get("/api/v1/homeworks/{id}/files/{fileName}", homeworkId.toString(), fileName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("File not found"));
    }
}