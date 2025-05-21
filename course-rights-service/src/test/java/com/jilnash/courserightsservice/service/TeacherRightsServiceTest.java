package com.jilnash.courserightsservice.service;

import com.jilnash.courserightsservice.model.Right;
import com.jilnash.courserightsservice.repo.RightRepository;
import com.jilnash.courserightsservice.repo.TeacherRightsRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TeacherRightsServiceTest {

    @Mock
    private TeacherRightsRepo teacherRightsRepo;

    @Mock
    private RightRepository rightRepo;

    @InjectMocks
    private TeacherRightsService teacherRightsService;

    public TeacherRightsServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRights_ShouldReturnRightsForGivenTeacherAndCourse() {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";
        Set<String> expectedRights = Set.of("READ", "WRITE");
        when(teacherRightsRepo.getTeacherRightsByTeacherIdAndCourseId(teacherId, courseId))
                .thenReturn(expectedRights);

        // Act
        Set<String> actualRights = teacherRightsService.getRights(courseId, teacherId);

        // Assert
        assertEquals(expectedRights, actualRights);
    }

    @Test
    void getRights_ShouldReturnEmptySetWhenNoRightsExists() {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";
        when(teacherRightsRepo.getTeacherRightsByTeacherIdAndCourseId(teacherId, courseId))
                .thenReturn(Collections.emptySet());

        // Act
        Set<String> actualRights = teacherRightsService.getRights(courseId, teacherId);

        // Assert
        assertEquals(Collections.emptySet(), actualRights);
    }


    @Test
    void hasRights_ShouldReturnTrueWhenTeacherHasAllRequiredRights() {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";
        Set<String> existingRights = Set.of("READ", "WRITE", "DELETE");
        Set<String> requiredRights = Set.of("READ", "WRITE");

        when(teacherRightsRepo.getTeacherRightsByTeacherIdAndCourseId(teacherId, courseId))
                .thenReturn(existingRights);

        // Act
        Boolean result = teacherRightsService.hasRights(courseId, teacherId, requiredRights);

        // Assert
        assertEquals(true, result);
    }

    @Test
    void hasRights_ShouldReturnFalseWhenTeacherDoesNotHaveAllRequiredRights() {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";
        Set<String> existingRights = Set.of("READ", "WRITE");
        Set<String> requiredRights = Set.of("READ", "DELETE");

        when(teacherRightsRepo.getTeacherRightsByTeacherIdAndCourseId(teacherId, courseId))
                .thenReturn(existingRights);

        // Act
        Boolean result = teacherRightsService.hasRights(courseId, teacherId, requiredRights);

        // Assert
        assertEquals(false, result);
    }

    @Test
    void setRights_ShouldSetRightsForTeacherAndCourse() {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";
        Set<String> rightsToSet = Set.of("READ", "WRITE");

        when(rightRepo.findAllByNameIn(rightsToSet))
                .thenReturn(java.util.Optional.of(Set.of(new Right(), new Right())));

        // Act
        Boolean result = teacherRightsService.setRights(courseId, teacherId, rightsToSet);

        // Assert
        assertEquals(true, result);
    }

    @Test
    void setRights_ShouldThrowExceptionWhenRightsNotFound() {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";
        Set<String> rightsToSet = Set.of("READ", "WRITE");

        when(rightRepo.findAllByNameIn(rightsToSet)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> teacherRightsService.setRights(courseId, teacherId, rightsToSet)
        );
    }

    @Test
    void setRights_ShouldThrowExceptionWhenRightsIsEmpty() {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";
        Set<String> rightsToSet = Collections.emptySet();

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> teacherRightsService.setRights(courseId, teacherId, rightsToSet)
        );
    }

    @Test
    void createCourseOwner_ShouldAssignAllRightsToTeacher() {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";
        List<Right> allRights = List.of(new Right(), new Right()); // Mocked Rights

        when(rightRepo.findAll()).thenReturn(allRights);

        // Act
        Boolean result = teacherRightsService.createCourseOwner(courseId, teacherId);

        // Assert
        assertEquals(true, result);
    }

    @Test
    void createCourseOwner_ShouldHandleEmptyRights() {
        // Arrange
        String teacherId = "teacher123";
        String courseId = "course456";

        when(rightRepo.findAll()).thenReturn(Collections.emptyList());

        // Act
        Boolean result = teacherRightsService.createCourseOwner(courseId, teacherId);

        // Assert
        assertEquals(true, result); // Should still return true even if no rights exist
    }
}