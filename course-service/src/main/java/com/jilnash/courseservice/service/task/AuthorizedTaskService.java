package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.client.FileClient;
import com.jilnash.courseservice.dto.task.*;
import com.jilnash.courseservice.mapper.TaskMapper;
import com.jilnash.courseservice.model.Task;
import com.jilnash.courseservice.service.courseauthr.CourseAuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * Service class that provides operations for managing tasks within a course module.
 * Ensures access control and validates user permissions for task actions.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizedTaskService {

    private final TaskServiceImpl taskService;

    private final TaskMapper taskMapper;

    private final CourseAuthorizationService courseAuthrService;

    private final FileClient fileClient;

    /**
     * Retrieves a list of tasks for a specific user based on the course, module, and task title criteria.
     * Ensures the user has proper access to the course before fetching the tasks.
     *
     * @param userId   the unique identifier of the user requesting the tasks
     * @param courseId the unique identifier of the course containing the tasks
     * @param moduleId the unique identifier of the module within the course
     * @param title    the title or title prefix of the tasks to search for
     * @return a list of TaskResponseDTO objects containing task details accessible
     */
    public List<TaskResponseDTO> getTasksForUser(String userId, String courseId, String moduleId, String title) {

        courseAuthrService.validateUserAccess(courseId, userId);

        log.info("[SERVICE] Fetching tasks in course {} module {} with title {}", courseId, moduleId, title);

        return taskService.getTasks(courseId, moduleId, title)
                .stream().map(taskMapper::toTaskResponse).toList();
    }

    /**
     * Retrieves a specific task for a user based on the course, module, and task identifiers.
     * Ensures the user has proper access to the course before fetching the task, if the task is not public.
     *
     * @param userId   the unique identifier of the user requesting the task
     * @param courseId the unique identifier of the course containing the task
     * @param moduleId the unique identifier of the module within the course
     * @param taskId   the unique identifier of the task to retrieve
     * @return a TaskResponseDTO object containing details of the requested task
     */
    public TaskResponseDTO getTaskForUser(String userId, String courseId, String moduleId, String taskId) {

        log.info("[SERVICE] Fetching task {} in course {} module {}", taskId, courseId, moduleId);

        Task task = taskService.getTask(courseId, moduleId, taskId);
        if (!task.getIsPublic())
            courseAuthrService.validateUserAccess(courseId, userId);

        task.setVideoLink(fileClient
                .getPreSignedUrl("course-project-tasks", taskVideoPath(taskId, task.getVideoLink())).join());


        return taskMapper.toTaskResponse(task);
    }

    private String taskVideoPath(String taskId, String fileName) {
        return "task-" + taskId + "/video/" + fileName;
    }

    /**
     * Retrieves the mathematical representation of task graph for a specific user based on the course and module identifiers.
     * Ensures the user has proper access to the course before fetching the task graph.
     *
     * @param userId   the unique identifier of the user requesting the task graph
     * @param courseId the unique identifier of the course containing the module
     * @param moduleId the unique identifier of the module within the course
     * @return the task graph for the specified course module
     */
    public TaskGraphDTO getTaskGraphForUser(String userId, String courseId, String moduleId) {

        courseAuthrService.validateUserAccess(courseId, userId);

        log.info("[SERVICE] Fetching task graph for course {} module {}", courseId, moduleId);

        return taskService.getTaskGraph(courseId, moduleId);
    }

    /**
     * Creates a task for a specified user within a course module.
     * Validates the user's permission to create a task in the course before proceeding.
     *
     * @param userId the unique identifier of the user creating the task
     * @param task   the TaskCreateDTO object containing details of the task to be created
     * @return a TaskCreateResponseDTO object containing details of the newly created task
     */
    public TaskCreateResponseDTO createTaskByUser(String userId, TaskCreateDTO task) {

        courseAuthrService.validateTeacherCourseRights(task.getCourseId(), userId, List.of("CREATE"));

        log.info("[SERVICE] Creating task in course {} module {}", task.getCourseId(), task.getModuleId());

        return taskMapper.toTaskCreateResponse(taskService.create(task));
    }

    /**
     * Updates the details of a task for a specific user in the context of a course module.
     * Validates the user's permission to update tasks in the course before proceeding.
     *
     * @param userId the unique identifier of the user attempting to update the task
     * @param task   the TaskUpdateDTO object containing the updated details of the task
     * @return a Boolean indicating whether the task was successfully updated
     */
    public Boolean updateTaskByUser(String userId, TaskUpdateDTO task) {

        courseAuthrService.validateTeacherCourseRights(task.getCourseId(), userId, List.of("UPDATE"));

        log.info("[SERVICE] Updating task {} in course {} module {}", task.getId(), task.getCourseId(), task.getModuleId());

        return taskService.update(task);
    }

    /**
     * Retrieves a list of prerequisites for a specific task in the context of a course module for a given user.
     * Ensures the user has proper access to the course before fetching the prerequisites.
     *
     * @param userId   the unique identifier of the user requesting the prerequisites
     * @param courseId the unique identifier of the course containing the task
     * @param moduleId the unique identifier of the module within the course
     * @param taskId   the unique identifier of the task whose prerequisites are being fetched
     * @return a list of prerequisite identifiers (as strings) for the specified task
     */
    public List<String> getPrereqsByUser(String userId, String courseId, String moduleId, String taskId) {

        courseAuthrService.validateStudentCourseAccess(courseId, userId);

        log.info("[SERVICE] Fetching prerequisites for task {} in course {} module {}", taskId, courseId, moduleId);

        return taskService.getTaskPrerequisites(courseId, moduleId, taskId);
    }

    /**
     * Updates the prerequisites for a specific task in a course module for a given user.
     * Ensures the user has the appropriate permissions to update the task prerequisites in the course.
     *
     * @param userId        the unique identifier of the user performing the update
     * @param courseId      the unique identifier of the course containing the task
     * @param moduleId      the unique identifier of the module within the course
     * @param taskId        the unique identifier of the task being updated
     * @param preRequisites a set of prerequisite identifiers to be associated with the task
     * @return a Boolean indicating whether the prerequisites were successfully updated
     */
    public Boolean updatePrereqsByUser(String userId, String courseId, String moduleId, String taskId, Set<String> preRequisites) {

        courseAuthrService.validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));

        log.info("[SERVICE] Updating prerequisites for task {} in course {} module {}", taskId, courseId, moduleId);

        return taskService.updateTaskPrerequisite(courseId, moduleId, taskId, preRequisites);
    }

    /**
     * Updates the title of a task for a specific user in a given course and module.
     *
     * @param teacherId the ID of the user attempting to update the task title
     * @param courseId  the ID of the course containing the task
     * @param moduleId  the ID of the module within the course containing the task
     * @param taskId    the ID of the task to update
     * @param title     the new title to set for the task
     * @return true if the task title was successfully updated, false otherwise
     */
    public Boolean updateTaskTitleByUser(String teacherId, String courseId, String moduleId, String taskId, String title) {

        courseAuthrService.validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));

        log.info("[SERVICE] Updating title for task {} in course {} module {}", taskId, courseId, moduleId);

        return taskService.updateTaskTitle(courseId, moduleId, taskId, title);
    }

    /**
     * Updates a task's video associated with a specific course module by a teacher.
     *
     * @param teacherId the unique identifier of the teacher requesting the update
     * @param courseId  the unique identifier of the course containing the module
     * @param moduleId  the unique identifier of the module containing the task
     * @param id        the unique identifier of the task whose video is being updated
     * @param video     the new video file to be uploaded and associated with the task
     * @return a Boolean indicating whether the update operation was successful
     */
    public Boolean updateTaskVideoByUser(String teacherId, String courseId, String moduleId, String id, MultipartFile video) {

        courseAuthrService.validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));

        log.info("[SERVICE] Updating video for task {} in course {} module {}", id, courseId, moduleId);

        return taskService.updateTaskVideo(courseId, moduleId, id, video);
    }

    /**
     * Updates the visibility status (isPublic) of a specific task within a module of a course
     * for a given teacher. The method validates the teacher's permissions for updating
     * the course before performing the operation.
     *
     * @param teacherId the identifier of the teacher requesting the update
     * @param courseId  the identifier of the course containing the task
     * @param moduleId  the identifier of the module containing the task
     * @param id        the identifier of the task to be updated
     * @param isPublic  the new visibility status to be set for the task (true for public, false for private)
     * @return a Boolean indicating whether the update operation was successful
     */
    public Boolean updateTaskIsPublicByUser(String teacherId, String courseId, String moduleId, String id, Boolean isPublic) {

        courseAuthrService.validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));

        log.info("[SERVICE] Updating isPublic for task {} in course {} module {}", id, courseId, moduleId);

        return taskService.updateTaskIsPublic(courseId, moduleId, id, isPublic);
    }

    /**
     * Updates the description of a specific task within a module and course.
     *
     * @param teacherId   The ID of the teacher performing the update, used to validate permissions.
     * @param courseId    The ID of the course to which the task belongs.
     * @param moduleId    The ID of the module containing the task.
     * @param id          The ID of the task whose description is to be updated.
     * @param description The new description to set for the task.
     * @return A Boolean value indicating whether the task description update was successful.
     */
    public Boolean updateTaskDescription(String teacherId, String courseId, String moduleId, String id, String description) {

        courseAuthrService.validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));

        log.info("[SERVICE] Updating description for task {} in course {} module {}", id, courseId, moduleId);

        return taskService.updateTaskDescription(courseId, moduleId, id, description);
    }
}
