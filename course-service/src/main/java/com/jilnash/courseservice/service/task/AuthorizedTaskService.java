package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.client.FileClient;
import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.dto.task.TaskGraphDTO;
import com.jilnash.courseservice.dto.task.TaskResponseDTO;
import com.jilnash.courseservice.dto.task.TaskUpdateDTO;
import com.jilnash.courseservice.mapper.TaskMapper;
import com.jilnash.courseservice.model.Task;
import com.jilnash.courseservice.service.courseauthr.CourseAuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizedTaskService {

    private final TaskServiceImpl taskService;

    private final CourseAuthorizationService courseAuthrService;

    private final FileClient fileClient;

    public List<TaskResponseDTO> getTasksForUser(String userId, String courseId, String moduleId, String title) {

        courseAuthrService.validateUserAccess(courseId, userId);

        log.info("[SERVICE] Fetching tasks in course {} module {} with title {}", courseId, moduleId, title);

        return taskService.getTasks(courseId, moduleId, title)
                .stream().map(TaskMapper::toTaskResponse).toList();
    }

    public TaskResponseDTO getTaskForUser(String userId, String courseId, String moduleId, String taskId) {

        log.info("[SERVICE] Fetching task {} in course {} module {}", taskId, courseId, moduleId);

        Task task = taskService.getTask(courseId, moduleId, taskId);

        task.setVideoLink(fileClient
                .getPreSignedUrl("course-project-tasks", taskVideoPath(taskId, task.getVideoLink())).join()
        );

        if (!task.getIsPublic())
            courseAuthrService.validateUserAccess(courseId, userId);

        return TaskMapper.toTaskResponse(task);
    }

    private String taskVideoPath(String taskId, String fileName) {
        return "task-" + taskId + "/video/" + fileName;
    }

    public TaskGraphDTO getTaskGraphForUser(String userId, String courseId, String moduleId) {

        courseAuthrService.validateUserAccess(courseId, userId);

        log.info("[SERVICE] Fetching task graph for course {} module {}", courseId, moduleId);

        return taskService.getTaskGraph(courseId, moduleId);
    }

    public Boolean createTaskByUser(String userId, TaskCreateDTO task) {

        courseAuthrService.validateTeacherCourseRights(task.getCourseId(), userId, List.of("CREATE"));

        log.info("[SERVICE] Creating task in course {} module {}", task.getCourseId(), task.getModuleId());

        return taskService.create(task);
    }

    public Boolean updateTaskByUser(String userId, TaskUpdateDTO task) {

        courseAuthrService.validateTeacherCourseRights(task.getCourseId(), userId, List.of("UPDATE"));

        log.info("[SERVICE] Updating task {} in course {} module {}", task.getId(), task.getCourseId(), task.getModuleId());

        return taskService.update(task);
    }

    public List<String> getPrereqsByUser(String userId, String courseId, String moduleId, String taskId) {

        courseAuthrService.validateStudentCourseAccess(courseId, userId);

        log.info("[SERVICE] Fetching prerequisites for task {} in course {} module {}", taskId, courseId, moduleId);

        return taskService.getTaskPrerequisites(courseId, moduleId, taskId);
    }

    public Boolean updatePrereqsByUser(String userId, String courseId, String moduleId, String taskId, Set<String> preRequisites) {

        courseAuthrService.validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));

        log.info("[SERVICE] Updating prerequisites for task {} in course {} module {}", taskId, courseId, moduleId);

        return taskService.updateTaskPrerequisite(courseId, moduleId, taskId, preRequisites);
    }

    public Boolean updateTaskTitleByUser(String userId, String courseId, String moduleId, String taskId, String title) {

        courseAuthrService.validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));

        log.info("[SERVICE] Updating title for task {} in course {} module {}", taskId, courseId, moduleId);

        return taskService.updateTaskTitle(courseId, moduleId, taskId, title);
    }

    public Boolean updateTaskVideoByUser(String teacherId, String courseId, String moduleId, String id, MultipartFile video) {

        courseAuthrService.validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));

        log.info("[SERVICE] Updating video for task {} in course {} module {}", id, courseId, moduleId);

        return taskService.updateTaskVideo(courseId, moduleId, id, video);
    }

    public Boolean updateTaskIsPublicByUser(String teacherId, String courseId, String moduleId, String id, Boolean isPublic) {

        courseAuthrService.validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));

        log.info("[SERVICE] Updating isPublic for task {} in course {} module {}", id, courseId, moduleId);

        return taskService.updateTaskIsPublic(courseId, moduleId, id, isPublic);
    }

    public Boolean updateTaskDescription(String teacherId, String courseId, String moduleId, String id, String description) {

        courseAuthrService.validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));

        log.info("[SERVICE] Updating description for task {} in course {} module {}", id, courseId, moduleId);

        return taskService.updateTaskDescription(courseId, moduleId, id, description);
    }
}
