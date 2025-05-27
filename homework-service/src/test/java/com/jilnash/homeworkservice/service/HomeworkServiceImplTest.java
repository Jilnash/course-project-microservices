package com.jilnash.homeworkservice.service;

import com.jilnash.homeworkservice.client.*;
import com.jilnash.homeworkservice.dto.HomeworkResponseDTO;
import com.jilnash.homeworkservice.mapper.HomeworkMapper;
import com.jilnash.homeworkservice.model.Homework;
import com.jilnash.homeworkservice.repo.HomeworkRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Date;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HomeworkServiceImplTest {

    private static final String HW_BUCKET = "test-bucket";
    @Mock
    private HomeworkMapper homeworkMapper;

    @Mock
    private HomeworkRepo homeworkRepo;

    @Mock
    private CourseClient courseClient;

    @Mock
    private CourseAccessGrpcClient courseAccessGrpcClient;

    @Mock
    private ProgressGrpcClient progressGrpcClient;

    @Mock
    private TaskReqsGrpcClient taskReqsGrpcClient;

    @Mock
    private FileClient fileClient;

    @Mock
    private HomeworkFileService homeworkFileService;

    @InjectMocks
    private HomeworkServiceImpl homeworkService;

    public HomeworkServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetHomeworks_WithAllFilters() {
        // Arrange
        String taskId = "123";
        String studentId = "456";
        Boolean checked = true;
        Date createdAfter = Date.valueOf("2023-01-01");

        Homework homework = Homework.builder()
                .id(UUID.randomUUID())
                .studentId(studentId)
                .taskId(taskId)
                .checked(true)
                .createdAt(Date.valueOf("2023-01-02"))
                .build();

        when(homeworkRepo.findAll(any(Specification.class))).thenReturn(List.of(homework));

        // Act
        List<Homework> result = homeworkService.getHomeworks(taskId, studentId, checked, createdAfter);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getStudentId()).isEqualTo(studentId);
        assertThat(result.getFirst().getTaskId()).isEqualTo(taskId);
        assertThat(result.getFirst().getChecked()).isTrue();
        assertThat(result.getFirst().getCreatedAt()).isAfterOrEqualTo(createdAfter);
    }

    @Test
    void testGetHomeworks_WithNullFilters() {
        // Arrange
        when(homeworkRepo.findAll(any(Specification.class))).thenReturn(new ArrayList<>());

        // Act
        List<Homework> result = homeworkService.getHomeworks(null, null, null, null);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void testGetHomeworks_WithPartialFilters() {
        // Arrange
        String studentId = "456";
        Date createdAfter = Date.valueOf("2023-01-01");

        Homework homework1 = Homework.builder()
                .id(UUID.randomUUID())
                .studentId(studentId)
                .taskId("789")
                .checked(false)
                .createdAt(Date.valueOf("2023-01-02"))
                .build();

        Homework homework2 = Homework.builder()
                .id(UUID.randomUUID())
                .studentId(studentId)
                .taskId("101112")
                .checked(true)
                .createdAt(Date.valueOf("2023-01-03"))
                .build();

        when(homeworkRepo.findAll(any(Specification.class))).thenReturn(Arrays.asList(homework1, homework2));

        // Act
        List<Homework> result = homeworkService.getHomeworks(null, studentId, null, createdAfter);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getStudentId()).isEqualTo(studentId);
        assertThat(result.get(1).getStudentId()).isEqualTo(studentId);
        assertThat(result.getFirst().getCreatedAt()).isAfterOrEqualTo(createdAfter);
        assertThat(result.get(1).getCreatedAt()).isAfterOrEqualTo(createdAfter);
    }

    @Test
    void testGetHomeworks_NoHomeworksFound() {
        // Arrange
        when(homeworkRepo.findAll(any(Specification.class))).thenReturn(new ArrayList<>());

        // Act
        List<Homework> result = homeworkService.getHomeworks("randomTaskId", "randomStudentId", false, null);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void testGetHomework_WithValidId() {
        // Arrange
        UUID validId = UUID.randomUUID();
        Homework homework = Homework.builder()
                .id(validId)
                .studentId("123")
                .taskId("456")
                .checked(true)
                .createdAt(Date.valueOf("2023-02-01"))
                .build();

        when(homeworkRepo.findById(validId)).thenReturn(java.util.Optional.of(homework));

        // Act
        Homework result = homeworkService.getHomework(validId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validId);
        assertThat(result.getStudentId()).isEqualTo("123");
        assertThat(result.getTaskId()).isEqualTo("456");
        assertThat(result.getChecked()).isTrue();
    }

    @Test
    void testGetHomework_WithInvalidId() {
        // Arrange
        UUID invalidId = UUID.randomUUID();

        when(homeworkRepo.findById(invalidId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> homeworkService.getHomework(invalidId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Homework not found with id: " + invalidId);
    }

    @Test
    void testGetHomeworkDTO_WithValidId() {
        // Arrange
        UUID validId = UUID.randomUUID();
        Homework homework = Homework.builder()
                .id(validId)
                .studentId("123")
                .taskId("456")
                .checked(true)
                .createdAt(Date.valueOf("2023-02-01"))
                .build();

        HomeworkResponseDTO responseDTO = HomeworkResponseDTO.builder().build();

        when(homeworkRepo.findById(validId)).thenReturn(Optional.of(homework));
        when(homeworkMapper.toResponseDTO(homework)).thenReturn(responseDTO);

        // Act
        HomeworkResponseDTO result = homeworkService.getHomeworkDTO(validId);

        // Assert
        assertThat(result).isNotNull();
        verify(homeworkMapper).toResponseDTO(homework);
    }

    @Test
    void testGetHomeworkDTO_WithInvalidId() {
        // Arrange
        UUID invalidId = UUID.randomUUID();

        when(homeworkRepo.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> homeworkService.getHomeworkDTO(invalidId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Homework not found with id: " + invalidId);
    }


    @Test
    void testSaveHomework_SuccessfulSave() {
        // Arrange
        Homework homework = Homework.builder()
                .studentId("123")
                .taskId("456")
                .files(new ArrayList<>())
                .build();

        doNothing().when(courseAccessGrpcClient)
                .validateStudentHasAccessToCourse(homework.getStudentId(), homework.getTaskId());

        when(progressGrpcClient.validateStudentCompletedTasks(homework.getStudentId(), List.of(homework.getTaskId())))
                .thenReturn(false);

        when(progressGrpcClient.validateStudentCompletedTasks(
                homework.getStudentId(),
                courseClient.getTaskPreRequisites(homework.getTaskId())
        )).thenReturn(true);

        when(homeworkRepo.getHwUnchecked(homework.getStudentId(), homework.getStudentId())).thenReturn(false);
        doNothing().when(taskReqsGrpcClient).validateAllTaskFilesProvided(homework);
        when(homeworkRepo.countByStudentIdAndTaskId(homework.getStudentId(), homework.getTaskId())).thenReturn(0);
        when(homeworkRepo.save(homework)).thenReturn(homework);
        doNothing().when(fileClient).uploadFile(any(), any(), any());
        doNothing().when(homeworkFileService).createdHomeworkFiles(homework);
        // Act
        Boolean result = homeworkService.saveHomework(homework);

        // Assert
        assertThat(result).isTrue();
        verify(homeworkRepo).save(homework);
    }

    @Test
    void testSaveHomework_ThrowsExceptionWhenStudentAlreadyCompletedTask() {
        // Arrange
        Homework homework = Homework.builder()
                .studentId("123")
                .taskId("456")
                .files(new ArrayList<>())
                .build();

        when(progressGrpcClient.validateStudentCompletedTasks(homework.getStudentId(), List.of(homework.getTaskId())))
                .thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> homeworkService.saveHomework(homework))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Student already completed this task");
    }

    @Test
    void testSaveHomework_ThrowsExceptionWhenPreRequisitesNotCompleted() {
        // Arrange
        Homework homework = Homework.builder()
                .studentId("123")
                .taskId("456")
                .files(new ArrayList<>())
                .build();

        when(progressGrpcClient.validateStudentCompletedTasks(
                homework.getStudentId(),
                courseClient.getTaskPreRequisites(homework.getTaskId())
        )).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> homeworkService.saveHomework(homework))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Student did not complete all prerequisites");
    }

    @Test
    void testSaveHomework_ThrowsExceptionWhenPreviousHomeworksUnchecked() {
        // Arrange
        Homework homework = Homework.builder()
                .studentId("123")
                .taskId("456")
                .files(new ArrayList<>())
                .build();


        when(progressGrpcClient.validateStudentCompletedTasks(homework.getStudentId(), List.of(homework.getTaskId())))
                .thenReturn(false);

        when(progressGrpcClient.validateStudentCompletedTasks(
                homework.getStudentId(),
                courseClient.getTaskPreRequisites(homework.getTaskId())
        )).thenReturn(true);

        when(homeworkRepo.getHwUnchecked(homework.getStudentId(), homework.getTaskId())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> homeworkService.saveHomework(homework))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Previous homework is not checked yet");
    }

    @Test
    void testSaveHomework_ThrowsExceptionWhenAllTaskReqsAreNotProvided() {
    }


    @Test
    void testGetHwTaskId_WithValidId() {
        // Arrange
        UUID validId = UUID.randomUUID();
        String taskId = "456";

        when(homeworkRepo.getHwTaskId(validId)).thenReturn(Optional.of(taskId));

        // Act
        String result = homeworkService.getHwTaskId(validId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(taskId);
        verify(homeworkRepo).getHwTaskId(validId);
    }

    @Test
    void testGetHwTaskId_WithInvalidId() {
        // Arrange
        UUID invalidId = UUID.randomUUID();

        when(homeworkRepo.getHwTaskId(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> homeworkService.getHwTaskId(invalidId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Homework not found with id: " + invalidId);
    }

    @Test
    void testGetHwStudentId_WithValidId() {
        // Arrange
        UUID validId = UUID.randomUUID();
        String studentId = "789";

        when(homeworkRepo.getHwStudentId(validId)).thenReturn(Optional.of(studentId));

        // Act
        String result = homeworkService.getHwStudentId(validId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(studentId);
        verify(homeworkRepo).getHwStudentId(validId);
    }

    @Test
    void testGetHwStudentId_WithInvalidId() {
        // Arrange
        UUID invalidId = UUID.randomUUID();

        when(homeworkRepo.getHwStudentId(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> homeworkService.getHwStudentId(invalidId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Homework not found with id: " + invalidId);
    }

    @Test
    void testSetChecked_WithValidId() {
        // Arrange
        UUID validId = UUID.randomUUID();
        Homework homework = Homework.builder()
                .id(validId)
                .checked(false)
                .build();

        when(homeworkRepo.findById(validId)).thenReturn(Optional.of(homework));
        when(homeworkRepo.save(any(Homework.class))).thenReturn(homework);

        // Act
        Boolean result = homeworkService.setChecked(validId);

        // Assert
        assertThat(result).isTrue();
        assertThat(homework.getChecked()).isTrue();
        verify(homeworkRepo).save(homework);
    }

    @Test
    void testSetChecked_WithInvalidId() {
        // Arrange
        UUID invalidId = UUID.randomUUID();

        when(homeworkRepo.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> homeworkService.setChecked(invalidId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Homework not found with id: " + invalidId);
    }
}