package com.jilnash.courseservice.service.task;

import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.dto.task.TaskGraphDTO;
import com.jilnash.courseservice.dto.task.TaskResponseDTO;
import com.jilnash.courseservice.dto.task.TaskUpdateDTO;
import com.jilnash.courseservice.mapper.TaskMapper;
import com.jilnash.courseservice.service.course.CourseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorizedTaskService {

    private final TaskServiceImpl taskService;

    private final CourseServiceImpl courseServiceImpl;

    public List<TaskResponseDTO> getTasksForUser(String userId, String courseId, String moduleId, String title) {

        courseServiceImpl.validateStudentCourseAccess(courseId, userId);
        //courseServiceImpl.validateTeacherCourseRights(courseId, userId, List.of("READ"));

        return taskService.getTasks(courseId, moduleId, title)
                .stream().map(TaskMapper::toTaskResponse).toList();
    }

    public TaskResponseDTO getTaskForUser(String userId, String courseId, String moduleId, String taskId) {

        courseServiceImpl.validateStudentCourseAccess(courseId, userId);
        //courseServiceImpl.validateTeacherCourseRights(courseId, userId, List.of("READ"));

        return TaskMapper.toTaskResponse(taskService.getTask(courseId, moduleId, taskId));
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
}
