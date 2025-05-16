package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.client.FileClient;
import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.dto.task.TaskCreateResponseDTO;
import com.jilnash.courseservice.dto.task.TaskResponseDTO;
import com.jilnash.courseservice.dto.task.TaskUpdateDTO;
import com.jilnash.courseservice.mapper.TaskMapper;
import com.jilnash.courseservice.model.Task;
import com.jilnash.courseservice.service.courseauthr.CourseAuthorizationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthorizedTaskServiceTest {

    @Mock
    private TaskServiceImpl taskService;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private CourseAuthorizationService courseAuthrService;

    @Mock
    private FileClient fileClient;

    @InjectMocks
    private AuthorizedTaskService authorizedTaskService;

    AuthorizedTaskServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTasksForUser_ValidInput_ReturnsTasks() {
        String userId = "user1";
        String courseId = "course1";
        String moduleId = "module1";
        String title = "taskTitle";

        Task mockTask = new Task();
        mockTask.setPrerequisites(Collections.emptyList());
        mockTask.setSuccessors(Collections.emptyList());

        List<Task> tasks = List.of(mockTask);
        TaskResponseDTO mockTaskResponse = new TaskResponseDTO();
        List<TaskResponseDTO> taskResponses = List.of(mockTaskResponse);

        when(taskService.getTasks(courseId, moduleId, title)).thenReturn(tasks);
        when(taskMapper.toTaskResponse(mockTask)).thenReturn(mockTaskResponse);

        List<TaskResponseDTO> result = authorizedTaskService.getTasksForUser(userId, courseId, moduleId, title);

        verify(courseAuthrService, times(1)).validateUserAccess(courseId, userId);
        verify(taskService, times(1)).getTasks(courseId, moduleId, title);
        assertEquals(taskResponses, result);
    }

    @Test
    void testGetTasksForUser_NoTasks_ReturnsEmptyList() {
        String userId = "user1";
        String courseId = "course1";
        String moduleId = "module1";
        String title = "taskTitle";

        doNothing().when(courseAuthrService).validateUserAccess(courseId, userId);
        when(taskService.getTasks(courseId, moduleId, title)).thenReturn(Collections.emptyList());

        List<TaskResponseDTO> result = authorizedTaskService.getTasksForUser(userId, courseId, moduleId, title);

        verify(courseAuthrService, times(1)).validateUserAccess(courseId, userId);
        verify(taskService, times(1)).getTasks(courseId, moduleId, title);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testGetTasksForUser_ValidationFails_ThrowsException() {
        String userId = "user1";
        String courseId = "course1";
        String moduleId = "module1";
        String title = "taskTitle";

        doThrow(new RuntimeException("Access Denied")).when(courseAuthrService).validateUserAccess(courseId, userId);

        try {
            authorizedTaskService.getTasksForUser(userId, courseId, moduleId, title);
        } catch (Exception e) {
            assertEquals("Access Denied", e.getMessage());
        }

        verify(courseAuthrService, times(1)).validateUserAccess(courseId, userId);
        verify(taskService, never()).getTasks(anyString(), anyString(), anyString());
    }

    @Test
    void testGetTaskForUser_ValidInput_ReturnsTask() {
        String userId = "user1";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "task1";
        String videoFileName = "videoFile.mp4";
        String expectedVideoLink = "signed-url";

        Task mockTask = new Task();
        mockTask.setId(taskId);
        mockTask.setVideoLink(videoFileName);
        mockTask.setIsPublic(true);

        TaskResponseDTO mockTaskResponse = new TaskResponseDTO();

        when(taskService.getTask(courseId, moduleId, taskId)).thenReturn(mockTask);
        when(fileClient.getPreSignedUrl("course-project-tasks", "task-task1/video/" + videoFileName))
                .thenReturn(java.util.concurrent.CompletableFuture.completedFuture(expectedVideoLink));
        when(taskMapper.toTaskResponse(mockTask)).thenReturn(mockTaskResponse);

        TaskResponseDTO result = authorizedTaskService.getTaskForUser(userId, courseId, moduleId, taskId);

        verify(taskService, times(1)).getTask(courseId, moduleId, taskId);
        verify(fileClient, times(1))
                .getPreSignedUrl("course-project-tasks", "task-task1/video/" + videoFileName);
        verify(taskMapper, times(1)).toTaskResponse(mockTask);
        verify(courseAuthrService, times(0)).validateUserAccess(anyString(), anyString());

        assertEquals(mockTaskResponse, result);
    }

    @Test
    void testGetTaskForUser_PrivateTask_ValidationFails_ThrowsException() {
        String userId = "user1";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "task1";

        Task mockTask = new Task();
        mockTask.setId(taskId);
        mockTask.setIsPublic(false);

        when(taskService.getTask(courseId, moduleId, taskId)).thenReturn(mockTask);
        doThrow(new RuntimeException("Access Denied")).when(courseAuthrService).validateUserAccess(courseId, userId);

        try {
            authorizedTaskService.getTaskForUser(userId, courseId, moduleId, taskId);
        } catch (Exception e) {
            assertEquals("Access Denied", e.getMessage());
        }

        verify(taskService, times(1)).getTask(courseId, moduleId, taskId);
        verify(courseAuthrService, times(1)).validateUserAccess(courseId, userId);
        verify(fileClient, never()).getPreSignedUrl(anyString(), anyString());
        verify(taskMapper, never()).toTaskResponse(any(Task.class));
    }

    @Test
    void testGetTaskForUser_PublicTask_DoesNotValidateAccess() {
        String userId = "user1";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "task1";
        String videoFileName = "videoFile.mp4";
        String expectedVideoLink = "signed-url";

        Task mockTask = new Task();
        mockTask.setId(taskId);
        mockTask.setVideoLink(videoFileName);
        mockTask.setIsPublic(true);

        TaskResponseDTO mockTaskResponse = new TaskResponseDTO();

        when(taskService.getTask(courseId, moduleId, taskId)).thenReturn(mockTask);
        when(fileClient.getPreSignedUrl("course-project-tasks", "task-task1/video/" + videoFileName))
                .thenReturn(java.util.concurrent.CompletableFuture.completedFuture(expectedVideoLink));
        when(taskMapper.toTaskResponse(mockTask)).thenReturn(mockTaskResponse);

        TaskResponseDTO result = authorizedTaskService.getTaskForUser(userId, courseId, moduleId, taskId);

        verify(taskService, times(1)).getTask(courseId, moduleId, taskId);
        verify(fileClient, times(1))
                .getPreSignedUrl("course-project-tasks", "task-task1/video/" + videoFileName);
        verify(taskMapper, times(1)).toTaskResponse(mockTask);
        verify(courseAuthrService, times(0)).validateUserAccess(anyString(), anyString());

        assertEquals(mockTaskResponse, result);
    }


    @Test
    void testCreateTaskByUser_ValidInput_ReturnsCreatedTask() {
        String userId = "teacher1";
        String courseId = "course1";

        TaskCreateDTO taskCreateDTO = new TaskCreateDTO();
        taskCreateDTO.setTitle("New Task");
        taskCreateDTO.setDescription("Task Description");
        taskCreateDTO.setFileRequirements(Collections.emptyList());
        taskCreateDTO.setVideoFile(mock(MultipartFile.class));
        taskCreateDTO.setIsPublic(true);
        taskCreateDTO.setCourseId(courseId);

        Task task = new Task();
        task.setId("task1");
        task.setTitle("New Task");
        TaskCreateResponseDTO taskCreateResponseDTO = new TaskCreateResponseDTO();

        doNothing().when(courseAuthrService).validateTeacherCourseRights(courseId, userId, List.of("CREATE"));
        when(taskService.create(taskCreateDTO)).thenReturn(task);
        when(taskMapper.toTaskCreateResponse(task)).thenReturn(taskCreateResponseDTO);

        TaskCreateResponseDTO result = authorizedTaskService.createTaskByUser(userId, taskCreateDTO);

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, userId, List.of("CREATE"));
        verify(taskService, times(1)).create(taskCreateDTO);

        assertEquals(taskCreateResponseDTO, result);
    }

    @Test
    void testCreateTaskByUser_InsufficientPermissions_ThrowsException() {
        String userId = "teacher2";
        String courseId = "course1";

        TaskCreateDTO taskCreateDTO = new TaskCreateDTO();
        taskCreateDTO.setCourseId(courseId);

        doThrow(new RuntimeException("Access Denied")).when(courseAuthrService)
                .validateTeacherCourseRights(courseId, userId, List.of("CREATE"));

        try {
            authorizedTaskService.createTaskByUser(userId, taskCreateDTO);
        } catch (Exception e) {
            assertEquals("Access Denied", e.getMessage());
        }

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, userId, List.of("CREATE"));
        verify(taskService, never()).create(any(TaskCreateDTO.class));
    }

    @Test
    void testUpdateTaskByUser_ValidInput_ReturnsUpdatedTask() {
        String userId = "teacher1";
        String courseId = "course1";
        String taskId = "task1";

        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
        taskUpdateDTO.setId(taskId);
        taskUpdateDTO.setCourseId(courseId);
        taskUpdateDTO.setTitle("Updated Task");
        taskUpdateDTO.setDescription("Updated Description");
        taskUpdateDTO.setVideoLink("updatedVideoLink");

        doNothing().when(courseAuthrService).validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));
        when(taskService.update(taskUpdateDTO)).thenReturn(true);

        Boolean result = authorizedTaskService.updateTaskByUser(userId, taskUpdateDTO);

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));
        verify(taskService, times(1)).update(taskUpdateDTO);

        assertEquals(true, result);
    }

    @Test
    void testUpdateTaskByUser_InsufficientPermissions_ThrowsException() {
        String userId = "teacher2";
        String courseId = "course1";
        String taskId = "task1";

        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
        taskUpdateDTO.setId(taskId);
        taskUpdateDTO.setCourseId(courseId);

        doThrow(new RuntimeException("Access Denied")).when(courseAuthrService)
                .validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));

        try {
            authorizedTaskService.updateTaskByUser(userId, taskUpdateDTO);
        } catch (Exception e) {
            assertEquals("Access Denied", e.getMessage());
        }

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));
        verify(taskService, never()).update(any(TaskUpdateDTO.class));
    }

    @Test
    void testUpdateTaskByUser_TaskNotFound_ThrowsException() {
        String userId = "teacher1";
        String courseId = "course1";
        String taskId = "taskMissing";

        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
        taskUpdateDTO.setId(taskId);
        taskUpdateDTO.setCourseId(courseId);

        doNothing().when(courseAuthrService).validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));
        when(taskService.update(taskUpdateDTO)).thenThrow(new RuntimeException("Task not found"));

        try {
            authorizedTaskService.updateTaskByUser(userId, taskUpdateDTO);
        } catch (Exception e) {
            assertEquals("Task not found", e.getMessage());
        }

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));
        verify(taskService, times(1)).update(taskUpdateDTO);
    }

    @Test
    void testGetPrerequisitesForUser_ValidInput_ReturnsPrerequisites() {
        String userId = "user1";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "task1";

        Task mockTask = new Task();
        mockTask.setId(taskId);
        mockTask.setPrerequisites(Collections.emptyList());

        List<String> prerequisites = List.of(mockTask.getId());

        doNothing().when(courseAuthrService).validateStudentCourseAccess(courseId, userId);
        when(taskService.getTaskPrerequisites(courseId, moduleId, taskId)).thenReturn(prerequisites);

        List<String> result = authorizedTaskService.getPrereqsByUser(userId, courseId, moduleId, taskId);

        verify(courseAuthrService, times(1)).validateStudentCourseAccess(courseId, userId);
        verify(taskService, times(1)).getTaskPrerequisites(courseId, moduleId, taskId);
        assertEquals(prerequisites, result);
    }

    @Test
    void testGetPrerequisites_InsufficientPermissions_ThrowsException() {
        String userId = "user1";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "task1";

        doThrow(new RuntimeException("Access Denied")).when(courseAuthrService)
                .validateStudentCourseAccess(courseId, userId);

        try {
            authorizedTaskService.getPrereqsByUser(userId, courseId, moduleId, taskId);
        } catch (Exception e) {
            assertEquals("Access Denied", e.getMessage());
        }

        verify(courseAuthrService, times(1)).validateStudentCourseAccess(courseId, userId);
        verify(taskService, never()).getTaskPrerequisites(anyString(), anyString(), anyString());
    }


    @Test
    void testUpdatePrereqsByUser_ValidInput_UpdatesSuccessfully() {
        String userId = "teacher1";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "task1";
        Set<String> prerequisites = Set.of("prereq1", "prereq2");

        doNothing().when(courseAuthrService).validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));
        when(taskService.updateTaskPrerequisite(courseId, moduleId, taskId, prerequisites)).thenReturn(true);

        Boolean result = authorizedTaskService.updatePrereqsByUser(userId, courseId, moduleId, taskId, prerequisites);

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));
        verify(taskService, times(1))
                .updateTaskPrerequisite(courseId, moduleId, taskId, prerequisites);

        assertEquals(true, result);
    }

    @Test
    void testUpdatePrereqsByUser_InsufficientPermissions_ThrowsException() {
        String userId = "teacher2";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "task1";
        Set<String> prerequisites = Set.of("prereq1", "prereq2");

        doThrow(new RuntimeException("Access Denied"))
                .when(courseAuthrService).validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));

        try {
            authorizedTaskService.updatePrereqsByUser(userId, courseId, moduleId, taskId, prerequisites);
        } catch (Exception e) {
            assertEquals("Access Denied", e.getMessage());
        }

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));
        verify(taskService, never())
                .updateTaskPrerequisite(anyString(), anyString(), anyString(), anySet());
    }

    @Test
    void testUpdatePrereqsByUser_TaskNotFound_ThrowsException() {
        String userId = "teacher1";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "taskMissing";
        Set<String> prerequisites = Set.of("prereq1", "prereq2");

        doNothing().when(courseAuthrService).validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));
        when(taskService.updateTaskPrerequisite(courseId, moduleId, taskId, prerequisites))
                .thenThrow(new RuntimeException("Task not found"));

        try {
            authorizedTaskService.updatePrereqsByUser(userId, courseId, moduleId, taskId, prerequisites);
        } catch (Exception e) {
            assertEquals("Task not found", e.getMessage());
        }

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));
        verify(taskService, times(1))
                .updateTaskPrerequisite(courseId, moduleId, taskId, prerequisites);
    }

    @Test
    void testUpdateTaskTitleByUser_ValidInput_UpdatesSuccessfully() {
        String teacherId = "teacher1";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "task1";
        String newTitle = "Updated Task Title";

        doNothing().when(courseAuthrService).validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        when(taskService.updateTaskTitle(courseId, moduleId, taskId, newTitle)).thenReturn(true);

        Boolean result = authorizedTaskService.updateTaskTitleByUser(teacherId, courseId, moduleId, taskId, newTitle);

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        verify(taskService, times(1)).updateTaskTitle(courseId, moduleId, taskId, newTitle);

        assertEquals(true, result);
    }

    @Test
    void testUpdateTaskTitleByUser_InsufficientPermissions_ThrowsException() {
        String teacherId = "teacher2";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "task1";
        String newTitle = "Updated Task Title";

        doThrow(new RuntimeException("Access Denied"))
                .when(courseAuthrService).validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));

        try {
            authorizedTaskService.updateTaskTitleByUser(teacherId, courseId, moduleId, taskId, newTitle);
        } catch (Exception e) {
            assertEquals("Access Denied", e.getMessage());
        }

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        verify(taskService, never()).updateTaskTitle(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testUpdateTaskTitleByUser_TaskNotFound_ThrowsException() {
        String teacherId = "teacher1";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "taskMissing";
        String newTitle = "Updated Task Title";

        doNothing().when(courseAuthrService).validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        when(taskService.updateTaskTitle(courseId, moduleId, taskId, newTitle))
                .thenThrow(new RuntimeException("Task not found"));

        try {
            authorizedTaskService.updateTaskTitleByUser(teacherId, courseId, moduleId, taskId, newTitle);
        } catch (Exception e) {
            assertEquals("Task not found", e.getMessage());
        }

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        verify(taskService, times(1)).updateTaskTitle(courseId, moduleId, taskId, newTitle);
    }

    @Test
    void testUpdateTaskVideoByUser_ValidInput_UpdatesSuccessfully() {
        String teacherId = "teacher1";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "task1";
        MultipartFile video = mock(MultipartFile.class);

        doNothing().when(courseAuthrService).validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        when(taskService.updateTaskVideo(courseId, moduleId, taskId, video)).thenReturn(true);

        Boolean result = authorizedTaskService.updateTaskVideoByUser(teacherId, courseId, moduleId, taskId, video);

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        verify(taskService, times(1))
                .updateTaskVideo(courseId, moduleId, taskId, video);

        assertEquals(true, result);
    }

    @Test
    void testUpdateTaskVideoByUser_InsufficientPermissions_ThrowsException() {
        String teacherId = "teacher2";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "task1";
        MultipartFile video = mock(MultipartFile.class);

        doThrow(new RuntimeException("Access Denied"))
                .when(courseAuthrService).validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));

        try {
            authorizedTaskService.updateTaskVideoByUser(teacherId, courseId, moduleId, taskId, video);
        } catch (Exception e) {
            assertEquals("Access Denied", e.getMessage());
        }

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        verify(taskService, never()).updateTaskVideo(anyString(), anyString(), anyString(), any(MultipartFile.class));
    }

    @Test
    void testUpdateTaskVideoByUser_TaskNotFound_ThrowsException() {
        String teacherId = "teacher1";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "taskMissing";
        MultipartFile video = mock(MultipartFile.class);

        doNothing().when(courseAuthrService).validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        when(taskService.updateTaskVideo(courseId, moduleId, taskId, video))
                .thenThrow(new RuntimeException("Task not found"));

        try {
            authorizedTaskService.updateTaskVideoByUser(teacherId, courseId, moduleId, taskId, video);
        } catch (Exception e) {
            assertEquals("Task not found", e.getMessage());
        }

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        verify(taskService, times(1))
                .updateTaskVideo(courseId, moduleId, taskId, video);
    }

    @Test
    void testUpdateTaskIsPublicByUser_ValidInput_UpdatesSuccessfully() {
        String teacherId = "teacher1";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "task1";
        Boolean isPublic = true;

        doNothing().when(courseAuthrService).validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        when(taskService.updateTaskIsPublic(courseId, moduleId, taskId, isPublic)).thenReturn(true);

        Boolean result = authorizedTaskService.updateTaskIsPublicByUser(teacherId, courseId, moduleId, taskId, isPublic);

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        verify(taskService, times(1))
                .updateTaskIsPublic(courseId, moduleId, taskId, isPublic);

        assertEquals(true, result);
    }

    @Test
    void testUpdateTaskIsPublicByUser_InsufficientPermissions_ThrowsException() {
        String teacherId = "teacher2";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "task1";
        Boolean isPublic = false;

        doThrow(new RuntimeException("Access Denied"))
                .when(courseAuthrService).validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));

        try {
            authorizedTaskService.updateTaskIsPublicByUser(teacherId, courseId, moduleId, taskId, isPublic);
        } catch (Exception e) {
            assertEquals("Access Denied", e.getMessage());
        }

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        verify(taskService, never())
                .updateTaskIsPublic(anyString(), anyString(), anyString(), anyBoolean());
    }

    @Test
    void testUpdateTaskIsPublicByUser_TaskNotFound_ThrowsException() {
        String teacherId = "teacher1";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "taskMissing";
        Boolean isPublic = false;

        doNothing().when(courseAuthrService).validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        when(taskService.updateTaskIsPublic(courseId, moduleId, taskId, isPublic))
                .thenThrow(new RuntimeException("Task not found"));

        try {
            authorizedTaskService.updateTaskIsPublicByUser(teacherId, courseId, moduleId, taskId, isPublic);
        } catch (Exception e) {
            assertEquals("Task not found", e.getMessage());
        }

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        verify(taskService, times(1))
                .updateTaskIsPublic(courseId, moduleId, taskId, isPublic);
    }

    @Test
    void testUpdateTaskDescriptionByUser_ValidInput_UpdatesSuccessfully() {
        String teacherId = "teacher1";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "task1";
        String newDescription = "Updated Task Description";

        doNothing().when(courseAuthrService).validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        when(taskService.updateTaskDescription(courseId, moduleId, taskId, newDescription)).thenReturn(true);

        Boolean result = authorizedTaskService.updateTaskDescription(teacherId, courseId, moduleId, taskId, newDescription);

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        verify(taskService, times(1)).updateTaskDescription(courseId, moduleId, taskId, newDescription);

        assertEquals(true, result);
    }

    @Test
    void testUpdateTaskDescriptionByUser_InsufficientPermissions_ThrowsException() {
        String teacherId = "teacher2";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "task1";
        String newDescription = "Updated Task Description";

        doThrow(new RuntimeException("Access Denied"))
                .when(courseAuthrService).validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));

        try {
            authorizedTaskService.updateTaskDescription(teacherId, courseId, moduleId, taskId, newDescription);
        } catch (Exception e) {
            assertEquals("Access Denied", e.getMessage());
        }

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        verify(taskService, never()).updateTaskDescription(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testUpdateTaskDescriptionByUser_TaskNotFound_ThrowsException() {
        String teacherId = "teacher1";
        String courseId = "course1";
        String moduleId = "module1";
        String taskId = "taskMissing";
        String newDescription = "Updated Task Description";

        doNothing().when(courseAuthrService).validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        when(taskService.updateTaskDescription(courseId, moduleId, taskId, newDescription))
                .thenThrow(new RuntimeException("Task not found"));

        try {
            authorizedTaskService.updateTaskDescription(teacherId, courseId, moduleId, taskId, newDescription);
        } catch (Exception e) {
            assertEquals("Task not found", e.getMessage());
        }

        verify(courseAuthrService, times(1))
                .validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));
        verify(taskService, times(1))
                .updateTaskDescription(courseId, moduleId, taskId, newDescription);
    }
}