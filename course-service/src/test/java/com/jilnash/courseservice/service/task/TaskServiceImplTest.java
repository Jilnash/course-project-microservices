package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.client.FileClient;
import com.jilnash.courseservice.client.ProgressGrpcClient;
import com.jilnash.courseservice.client.TaskReqGrpcClient;
import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.dto.task.TaskGraphDTO;
import com.jilnash.courseservice.dto.task.TaskUpdateDTO;
import com.jilnash.courseservice.model.Task;
import com.jilnash.courseservice.repo.TaskRepo;
import com.jilnash.courseservice.service.module.ModuleServiceImpl;
import com.jilnash.taskfilerequirementsservice.dto.TaskFileReqDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock
    private TaskRepo taskRepo;
    @Mock
    private ModuleServiceImpl moduleService;
    @Mock
    private FileClient fileClient;
    @Mock
    private ProgressGrpcClient progressGrpcClient;
    @Mock
    private TaskReqGrpcClient taskReqGrpcClient;

    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTasks() {
        // Arrange
        String courseId = "courseId";
        String moduleId = "moduleId";
        String title = "title";
        List<Task> expectedTasks = List.of(new Task());
        when(taskRepo.findAllByTitleStartingWithAndModule_IdAndModule_Course_Id(title, moduleId, courseId))
                .thenReturn(expectedTasks);

        // Act
        List<Task> tasks = taskServiceImpl.getTasks(courseId, moduleId, title);

        // Assert
        assertEquals(expectedTasks, tasks);
        verify(taskRepo, times(1))
                .findAllByTitleStartingWithAndModule_IdAndModule_Course_Id(title, moduleId, courseId);
    }

    @Test
    void testGetTaskById() {
        // Arrange
        Task task = new Task();
        task.setId("taskId");
        when(taskRepo.findById("taskId")).thenReturn(Optional.of(task));

        // Act
        Task result = taskServiceImpl.getTask("taskId");

        // Assert
        assertEquals(task, result);
        verify(taskRepo, times(1)).findById("taskId");
    }

    @Test
    void testGetTaskByIdNotFound() {
        // Arrange
        when(taskRepo.findById("taskId")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> taskServiceImpl.getTask("taskId"));
        assertEquals("Task not found", exception.getMessage());
        verify(taskRepo, times(1)).findById("taskId");
    }

    @Test
    void testCreateTask() {
        // Arrange
        TaskCreateDTO taskCreateDTO = new TaskCreateDTO();
        taskCreateDTO.setModuleId("moduleId");
        taskCreateDTO.setCourseId("courseId");
        taskCreateDTO.setVideoFile(mock(MultipartFile.class));
        taskCreateDTO.setPrerequisiteTasksIds(Collections.emptySet());
        taskCreateDTO.setFileRequirements(List.of(new TaskFileReqDTO("image/jpeg", (short) 1)));
        taskCreateDTO.setSuccessorTasksIds(Collections.emptySet());

        Task expectedTask = new Task();
        doNothing().when(taskReqGrpcClient).setTaskRequirements(any(), any());
        when(taskRepo.createTaskWithoutRelationships(any())).thenReturn(Optional.of(expectedTask));

        // Act
        Task result = taskServiceImpl.create(taskCreateDTO);

        // Assert
        assertNotNull(result);
        verify(moduleService, times(1)).validateModuleExistsInCourse("moduleId", "courseId");
        verify(fileClient, times(1)).uploadFiles(eq("course-project-tasks"), anyString(), anyList());
        verify(taskRepo, times(1)).createTaskWithoutRelationships(taskCreateDTO);
    }

    @Test
    void testUpdateTask() {

        String taskId = "taskId";
        String moduleId = "moduleId";
        String courseId = "courseId";

        // Arrange
        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
        taskUpdateDTO.setId(taskId);
        taskUpdateDTO.setModuleId(moduleId);
        taskUpdateDTO.setCourseId(courseId);
        taskUpdateDTO.setTitle("New Title");
        taskUpdateDTO.setDescription("New Description");
        taskUpdateDTO.setVideoLink("videoLink");

        when(taskRepo.findByIdAndModule_IdAndModule_Course_Id(taskId, moduleId, courseId))
                .thenReturn(Optional.of(new Task()));
        when(taskRepo.updateTaskData(taskId, "New Title", "New Description", "videoLink"))
                .thenReturn(new Task());

        // Act
        Boolean result = taskServiceImpl.update(taskUpdateDTO);

        // Assert
        assertTrue(result);
        verify(taskRepo, times(1))
                .updateTaskData("taskId", "New Title", "New Description", "videoLink");
    }

    @Test
    void testGetTaskGraph() {
        // Arrange
        String courseId = "courseId";
        String moduleId = "moduleId";
        Task task1 = new Task();
        task1.setId("task1");
        Task task2 = new Task();
        task2.setId("task2");
        task1.setSuccessors(List.of(task2));
        task2.setSuccessors(Collections.emptyList());

        when(taskRepo.findAllByModule_IdAndModule_Course_Id(moduleId, courseId)).thenReturn(List.of(task1, task2));

        // Act
        TaskGraphDTO graph = taskServiceImpl.getTaskGraph(courseId, moduleId);

        // Assert
        assertEquals(2, graph.getNodes().size());
        assertEquals(1, graph.getEdges().size());
        verify(taskRepo, times(1)).findAllByModule_IdAndModule_Course_Id(moduleId, courseId);
    }

    @Test
    void testGetTaskGraphWithNoTasks() {
        // Arrange
        String courseId = "courseId";
        String moduleId = "moduleId";
        when(taskRepo.findAllByModule_IdAndModule_Course_Id(moduleId, courseId)).thenReturn(Collections.emptyList());

        // Act
        TaskGraphDTO graph = taskServiceImpl.getTaskGraph(courseId, moduleId);

        // Assert
        assertTrue(graph.getNodes().isEmpty());
        assertTrue(graph.getEdges().isEmpty());
        verify(taskRepo, times(1)).findAllByModule_IdAndModule_Course_Id(moduleId, courseId);
    }
}