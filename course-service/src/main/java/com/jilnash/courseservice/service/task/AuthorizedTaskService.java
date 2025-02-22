package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.dto.task.TaskGraphDTO;
import com.jilnash.courseservice.dto.task.TaskResponseDTO;
import com.jilnash.courseservice.dto.task.TaskUpdateDTO;
import com.jilnash.courseservice.mapper.TaskMapper;
import com.jilnash.courseservice.service.course.CourseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorizedTaskService {

    private final TaskServiceImpl taskService;

    private final CourseServiceImpl courseServiceImpl;

    public List<TaskResponseDTO> getTasksForUser(String userId, String courseId, String moduleId, String title) {

        courseServiceImpl.validateUserAccess(courseId, userId);

        return taskService.getTasks(courseId, moduleId, title)
                .stream().map(TaskMapper::toTaskResponse).toList();
    }

    public TaskResponseDTO getTaskForUser(String userId, String courseId, String moduleId, String taskId) {

        var task = taskService.getTask(courseId, moduleId, taskId);

        if (!task.getIsPublic())
            courseServiceImpl.validateUserAccess(courseId, userId);

        return TaskMapper.toTaskResponse(task);
    }

    public TaskGraphDTO getTaskGraphForUser(String userId, String courseId, String moduleId) {

        courseServiceImpl.validateStudentCourseAccess(courseId, userId);

        return taskService.getTaskGraph(courseId, moduleId);
    }

    public Boolean createTaskByUser(String userId, TaskCreateDTO task) {

        courseServiceImpl.validateTeacherCourseRights(task.getCourseId(), userId, List.of("CREATE"));

        return taskService.create(task);
    }

    public Boolean updateTaskByUser(String userId, TaskUpdateDTO task) {

        courseServiceImpl.validateTeacherCourseRights(task.getCourseId(), userId, List.of("UPDATE"));

        return taskService.update(task);
    }

    public List<String> getPrereqsByUser(String userId, String courseId, String moduleId, String taskId) {

        courseServiceImpl.validateStudentCourseAccess(courseId, userId);

        return taskService.getTaskPrerequisites(courseId, moduleId, taskId);
    }

    public Boolean updatePrereqsByUser(String userId, String courseId, String moduleId, String taskId, Set<String> preRequisites) {

        courseServiceImpl.validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));

        return taskService.updateTaskPrerequisite(courseId, moduleId, taskId, preRequisites);
    }

    public Boolean updateTaskTitleByUser(String userId, String courseId, String moduleId, String taskId, String title) {

        courseServiceImpl.validateTeacherCourseRights(courseId, userId, List.of("UPDATE"));

        return taskService.updateTaskTitle(courseId, moduleId, taskId, title);
    }

    public Boolean updateTaskVideoByUser(String teacherId, String courseId, String moduleId, String id, MultipartFile video) {

        courseServiceImpl.validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));

        return taskService.updateTaskVideo(courseId, moduleId, id, video);
    }

    public Boolean updateTaskIsPublicByUser(String teacherId, String courseId, String moduleId, String id, Boolean isPublic) {

        courseServiceImpl.validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));

        return taskService.updateTaskIsPublic(courseId, moduleId, id, isPublic);
    }

    public Boolean updateTaskDescription(String teacherId, String courseId, String moduleId, String id, String description) {

        courseServiceImpl.validateTeacherCourseRights(courseId, teacherId, List.of("UPDATE"));

        return taskService.updateTaskDescription(courseId, moduleId, id, description);
    }
}
