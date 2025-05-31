package com.jilnash.hwresponseservice.service;

import com.jilnash.hwresponseservice.clients.CourseClient;
import com.jilnash.hwresponseservice.clients.CourseRightsGrpcClient;
import com.jilnash.hwresponseservice.clients.HwClient;
import com.jilnash.hwresponseservice.clients.ProgressGrpcClient;
import com.jilnash.hwresponseservice.model.mongo.HwResponse;
import com.jilnash.hwresponseservice.repo.HwResponseRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@DataMongoTest
class HwResponseServiceImplTest {

    @Mock
    private HwResponseRepo hwResponseRepo;

    @Mock
    private HwClient hwClient;

    @Mock
    private CourseRightsGrpcClient courseRightsGrpcClient;

    @Mock
    private ProgressGrpcClient progressGrpcClient;

    @Mock
    private CourseClient courseClient;

    @InjectMocks
    private HwResponseServiceImpl hwResponseService;

    public HwResponseServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetResponseSuccessful() {
        // Arrange
        String responseId = "123";
        HwResponse response = HwResponse.builder().id(responseId).build();
        when(hwResponseRepo.findById(responseId)).thenReturn(Optional.of(response));

        // Act
        HwResponse retrievedResponse = hwResponseService.getResponse(responseId);

        // Assert
        assertNotNull(retrievedResponse);
        assertEquals(responseId, retrievedResponse.getId());
        verify(hwResponseRepo, times(1)).findById(responseId);
    }

    @Test
    void testGetResponseNotFound() {
        // Arrange
        String responseId = "nonexistent";
        when(hwResponseRepo.findById(responseId)).thenReturn(Optional.empty());

        // Act
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> hwResponseService.getResponse(responseId));

        // Assert
        assertEquals("Response with id " + responseId + " not found", exception.getMessage());
        verify(hwResponseRepo, times(1)).findById(responseId);
    }


    @Test
    void testCreateResponseSuccessful() {
        // Arrange
        HwResponse validResponse = HwResponse.builder()
                .id("123")
                .teacherId("teacher1")
                .homeworkId("homework1")
                .isCorrect(true)
                .build();

        String taskId = "task123";
        when(hwClient.getTaskId(validResponse.getHomeworkId())).thenReturn(taskId);
        when(hwResponseRepo.existsByHomeworkId(validResponse.getHomeworkId())).thenReturn(false);
        when(courseClient.getTaskCourseId(validResponse.getHomeworkId())).thenReturn(taskId);

        doNothing().when(courseRightsGrpcClient)
                .validateTeacherAllowedToCheckHomework(anyString(), eq(validResponse.getTeacherId()));

        when(hwClient.getStudentId(validResponse.getHomeworkId())).thenReturn("student1");
        doNothing().when(progressGrpcClient).addTaskToStudentProgress("student1", taskId);
        doNothing().when(hwClient).setChecked(validResponse.getHomeworkId());

        // Act
        Boolean result = hwResponseService.createResponse(validResponse);

        // Assert
        assertTrue(result);
        verify(hwResponseRepo, times(1)).save(validResponse);
        verify(hwClient, times(1)).setChecked(validResponse.getHomeworkId());
    }

    @Test
    void testCreateResponseHomeworkAlreadyChecked() {
        // Arrange
        HwResponse response = HwResponse.builder().homeworkId("homework1").build();

        when(hwResponseRepo.existsByHomeworkId(response.getHomeworkId())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> hwResponseService.createResponse(response));

        assertEquals("Homework already checked", exception.getMessage());
        verify(hwResponseRepo, never()).save(response);
    }

    @Test
    void testCreateResponseHwResponseExists() {
        // Arrange
        HwResponse response = HwResponse.builder()
                .homeworkId("homework1")
                .teacherId("teacher1")
                .build();

        String taskId = "task123";
        when(hwClient.getTaskId(response.getHomeworkId())).thenReturn(taskId);
        when(hwResponseRepo.existsByHomeworkId(response.getHomeworkId())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> hwResponseService.createResponse(response));

        assertEquals("Homework already checked", exception.getMessage());
        verify(courseRightsGrpcClient, never())
                .validateTeacherAllowedToCheckHomework("task123", response.getTeacherId());
        verify(hwResponseRepo, never()).save(response);
    }

    @Test
    void testCreateResponseTeacherNotAllowed() {
        // Arrange
        HwResponse response = HwResponse.builder()
                .homeworkId("homework1")
                .teacherId("teacher1")
                .build();

        String taskId = "task123";
        when(hwClient.getTaskId(response.getHomeworkId())).thenReturn(taskId);
        when(hwResponseRepo.existsByHomeworkId(response.getHomeworkId())).thenReturn(false);
        when(courseClient.getTaskCourseId(taskId)).thenReturn("course123");

        doThrow(new IllegalArgumentException("Teacher not allowed to respond to homework"))
                .when(courseRightsGrpcClient)
                .validateTeacherAllowedToCheckHomework(eq("course123"), eq(response.getTeacherId()));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> hwResponseService.createResponse(response));

        assertEquals("Teacher not allowed to respond to homework", exception.getMessage());
        verify(hwResponseRepo, never()).save(response);
    }

    @Test
    void testUpdateResponseSuccessful() {
        // Arrange
        HwResponse existingResponse = HwResponse.builder()
                .id("123")
                .teacherId("teacher1")
                .homeworkId("homework1")
                .isCorrect(true)
                .build();

        HwResponse updatedResponse = HwResponse.builder()
                .id("123")
                .teacherId("teacher1")
                .homeworkId("homework1")
                .isCorrect(false)
                .build();

        when(hwResponseRepo.findById(existingResponse.getId())).thenReturn(Optional.of(existingResponse));
        doNothing().when(courseRightsGrpcClient)
                .validateTeacherAllowedToCheckHomework(anyString(), eq(updatedResponse.getTeacherId()));
        when(hwClient.getTaskId(updatedResponse.getHomeworkId())).thenReturn("task123");

        // Act
        Boolean result = hwResponseService.updateResponse(updatedResponse);

        // Assert
        assertTrue(result);
        verify(hwResponseRepo, times(1)).save(updatedResponse);
    }

    @Test
    void testUpdateResponseNotFound() {
        // Arrange
        HwResponse response = HwResponse.builder().id("nonexistent").build();
        when(hwResponseRepo.findById(response.getId())).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> hwResponseService.updateResponse(response));

        assertEquals("Response with id nonexistent not found", exception.getMessage());
        verify(hwResponseRepo, never()).save(response);
    }

    @Test
    void testUpdateResponseTeacherNotAllowedToCheck() {

        // Arrange
        HwResponse response = HwResponse.builder()
                .id("response1")
                .teacherId("teacher1")
                .isCorrect(true)
                .build();
        when(hwResponseRepo.findById(response.getId())).thenReturn(Optional.of(response));
        when(hwClient.getTaskId(response.getHomeworkId())).thenReturn("task123");
        when(courseClient.getTaskCourseId(eq("task123"))).thenReturn("course123");
        doThrow(new IllegalArgumentException("Teacher not allowed to check this response"))
                .when(courseRightsGrpcClient)
                .validateTeacherAllowedToCheckHomework(eq("course123"), eq(response.getTeacherId()));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> hwResponseService.updateResponse(response));

        assertEquals("Teacher not allowed to check this response", exception.getMessage());
        verify(hwResponseRepo, never()).save(response);
    }
}