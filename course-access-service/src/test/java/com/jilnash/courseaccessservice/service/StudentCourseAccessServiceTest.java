package com.jilnash.courseaccessservice.service;

import com.jilnash.courseaccessservice.model.StudentCourseAccess;
import com.jilnash.courseaccessservice.repo.StudentCourseAccessRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

//@SpringBootTest
public class StudentCourseAccessServiceTest {

    @Mock
    private StudentCourseAccessRepo studentCourseAccessRepo;

    @InjectMocks
    private StudentCourseAccessService studentCourseAccessService;

    public StudentCourseAccessServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStudentHasAccessWhenAccessExists() {
        // Arrange
        String studentId = "student123";
        String courseId = "course321";
        Date currentDate = new Date(System.currentTimeMillis());
        when(studentCourseAccessRepo.existsByStudentIdAndCourseIdAndStartDateBeforeAndEndDateAfter(
                eq(studentId), eq(courseId), any(Date.class)
        )).thenReturn(true);

        // Act
        Boolean result = studentCourseAccessService.getStudentHasAccess(studentId, courseId);

        // Assert
        assertTrue(result);
    }

    @Test
    void testGetStudentHasAccessWhenAccessDoesNotExist() {
        // Arrange
        String studentId = "student123";
        String courseId = "course321";
        Date currentDate = new Date(System.currentTimeMillis());
        when(studentCourseAccessRepo.existsByStudentIdAndCourseIdAndStartDateBeforeAndEndDateAfter(
                studentId, courseId, currentDate
        )).thenReturn(false);

        // Act
        Boolean result = studentCourseAccessService.getStudentHasAccess(studentId, courseId);

        // Assert
        assertFalse(result);
    }

    @Test
    void testGetStudentHasAccessWhenStudentIdIsNull() {
        // Arrange
        String studentId = null;
        String courseId = "course321";
        Date currentDate = new Date(System.currentTimeMillis());
        when(studentCourseAccessRepo.existsByStudentIdAndCourseIdAndStartDateBeforeAndEndDateAfter(
                studentId, courseId, currentDate
        )).thenReturn(false);

        // Act
        Boolean result = studentCourseAccessService.getStudentHasAccess(studentId, courseId);

        // Assert
        assertFalse(result);
    }

    @Test
    void testGetStudentHasAccessWhenCourseIdIsNull() {
        // Arrange
        String studentId = "student123";
        String courseId = null;
        Date currentDate = new Date(System.currentTimeMillis());
        when(studentCourseAccessRepo.existsByStudentIdAndCourseIdAndStartDateBeforeAndEndDateAfter(
                studentId, courseId, currentDate
        )).thenReturn(false);

        // Act
        Boolean result = studentCourseAccessService.getStudentHasAccess(studentId, courseId);

        // Assert
        assertFalse(result);
    }

    @Test
    void testPurchaseSuccessful() {
        // Arrange
        StudentCourseAccess studentCourseAccess = StudentCourseAccess.builder()
                .id(1L)
                .studentId("student123")
                .courseId("course321")
                .build();
        when(studentCourseAccessRepo.existsByStudentIdAndCourseIdAndStartDateBeforeAndEndDateAfter(
                eq("student123"), eq("course321"), any(Date.class)
        )).thenReturn(false);
        when(studentCourseAccessRepo.save(any(StudentCourseAccess.class))).thenReturn(studentCourseAccess);

        // Act
        StudentCourseAccess result = studentCourseAccessService.purchase(studentCourseAccess);

        // Assert
        assertEquals(1L, (long) result.getId());
        assertEquals("student123", result.getStudentId());
        assertEquals("course321", result.getCourseId());
    }

    @Test
    void testPurchaseThrowsExceptionWhenAlreadyHasAccess() {
        // Arrange
        StudentCourseAccess studentCourseAccess = StudentCourseAccess.builder()
                .id(1L)
                .studentId("student123")
                .courseId("course321")
                .build();
        when(studentCourseAccessRepo.existsByStudentIdAndCourseIdAndStartDateBeforeAndEndDateAfter(
                eq("student123"), eq("course321"), any(Date.class)
        )).thenReturn(true);

        // Act and Assert
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> studentCourseAccessService.purchase(studentCourseAccess)
        );
        assertTrue(exception.getMessage().contains("Student already has access to this course"));
    }
}