package com.jilnash.courseservice.listener;

import com.jilnash.courseservice.mapper.TaskMapper;
import com.jilnash.courseservice.model.Task;
import com.jilnash.courseservice.service.task.TaskService;
import com.jilnash.courseservicedto.dto.task.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskListener {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    public TaskListener(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @KafkaListener(topics = "task-create-topic", groupId = "course-saga-group")
    public void createTaskListener(TaskCreateDTO taskCreateDTO) {
        try {
            taskService.createTask(taskCreateDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error creating task with name: " + taskCreateDTO.getTitle(), e);
        }
    }

    @KafkaListener(topics = "task-update-title-topic", groupId = "course-saga-group")
    public void updateTaskTitleListener(TaskUpdateTitleDTO dto) {
        try {
            taskService.updateTaskTitle(dto.courseId(), dto.moduleId(), dto.taskId(), dto.title());
        } catch (Exception e) {
            throw new RuntimeException("Error updating task title for ID: " + dto.taskId(), e);
        }
    }

    @KafkaListener(topics = "task-update-description-topic", groupId = "course-saga-group")
    public void updateTaskDescriptionListener(TaskUpdateDescriptionDTO dto) {
        try {
            taskService.updateTaskDescription(dto.courseId(), dto.moduleId(), dto.taskId(), dto.description());
        } catch (Exception e) {
            throw new RuntimeException("Error updating task description for ID: " + dto.taskId(), e);
        }
    }

    @KafkaListener(topics = "task-update-video-file-topic", groupId = "course-saga-group")
    public void updateTaskVideoFileListener(TaskUpdateVideoFileDTO dto) {
        try {
            taskService.updateTaskVideoFileName(dto.courseId(), dto.moduleId(), dto.id(), dto.videoFileName());
        } catch (Exception e) {
            throw new RuntimeException("Error updating video file for task ID: " + dto.id(), e);
        }
    }

    @KafkaListener(topics = "task-update-is-public-topic", groupId = "course-saga-group")
    public void updateTaskIsPublicListener(TaskUpdateIsPublicDTO dto) {
        try {
            taskService.updateTaskIsPublic(dto.courseId(), dto.moduleId(), dto.id(), dto.isPublic());
        } catch (Exception e) {
            throw new RuntimeException("Error updating task visibility for ID: " + dto.id(), e);
        }
    }

    @KafkaListener(topics = "task-update-hw-posting-interval-topic", groupId = "course-saga-group")
    public void updateTaskHwPostingIntervalListener(TaskUpdateHwIntervalDTO dto) {
        try {
            taskService.updateTaskHwPostingInterval(dto.courseId(), dto.moduleId(), dto.taskId(), dto.hwPostingInterval());
        } catch (Exception e) {
            throw new RuntimeException("Error updating HW posting interval for task ID: " + dto.taskId(), e);
        }
    }

    @KafkaListener(topics = "task-update-prerequisites-topic", groupId = "course-saga-group")
    public void updateTaskPrerequisitesListener(TaskUpdatePrereqsDTO dto) {
        try {
            taskService.updateTaskPrerequisites(dto.courseId(), dto.moduleId(), dto.taskId(), dto.prerequisiteTaskIds());
        } catch (Exception e) {
            throw new RuntimeException("Error updating prerequisites for task ID: " + dto.taskId(), e);
        }
    }

    @KafkaListener(topics = "task-update-successors-topic", groupId = "course-saga-group")
    public void updateTaskSuccessorsListener(TaskUpdateSuccessorsDTO dto) {
        try {
            taskService.updateTaskSuccessors(dto.courseId(), dto.moduleId(), dto.taskId(), dto.successorTasksIds());
        } catch (Exception e) {
            throw new RuntimeException("Error updating successors for task ID: " + dto.taskId(), e);
        }
    }

    @KafkaListener(topics = "task-soft-delete-topic", groupId = "course-saga-group")
    public void softDeleteTaskListener(TaskDeleteDTO dto) {
        try {
            taskService.softDeleteTask(dto.courseId(), dto.moduleId(), dto.taskId());
        } catch (Exception e) {
            throw new RuntimeException("Error performing soft delete for task ID: " + dto.taskId(), e);
        }
    }

    @KafkaListener(topics = "task-hard-delete-topic", groupId = "course-saga-group")
    public void hardDeleteTaskListener(TaskDeleteDTO dto) {
        try {
            taskService.hardDeleteTask(dto.courseId(), dto.moduleId(), dto.taskId());
        } catch (Exception e) {
            throw new RuntimeException("Error performing hard delete for task ID: " + dto.taskId(), e);
        }
    }

    @KafkaListener(topics = "task-get-tasks-topic", groupId = "course-saga-group")
    public List<Task> getTasksListener(TasksRequestDTO dto) {
        try {
            return taskService.getTasks(dto.courseId(), dto.moduleId(), dto.name());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving tasks.", e);
        }
    }

    @KafkaListener(
            topics = "task-get-tasks-as-graph-topic",
            groupId = "course-saga-group",
            containerFactory = "courseListenerContainerFactory"
    )
    @SendTo("task-graph-reply-topic")
    public TaskGraph getTasksAsGraphListener(TaskGraphRequestDTO dto) {
        try {
            return taskService.getTasksAsGraph(dto.courseId(), dto.moduleId());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving tasks as graph.", e);
        }
    }

    @KafkaListener(topics = "task-get-task-by-id-topic", groupId = "course-saga-group")
    public Task getTaskByIdListener(String id) {
        try {
            return taskService.getTask(id);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving task by ID: " + id, e);
        }
    }

    @KafkaListener(
            topics = "task-get-task-by-course-module-id-topic",
            groupId = "course-saga-group",
            containerFactory = "courseListenerContainerFactory"
    )
    @SendTo("task-reply-topic")
    public TaskResponse getTaskByCourseModuleIdListener(TaskRequestDTO dto) {
        try {
            return taskMapper.toTaskResponse(taskService.getTask(dto.courseId(), dto.moduleId(), dto.id()));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving task for course: "
                    + dto.courseId() + ", module: " + dto.moduleId() + ", ID: " + dto.id(), e);
        }
    }

    @KafkaListener(topics = "task-get-title-topic", groupId = "course-saga-group")
    public void getTaskTitleListener(String id) {
        try {
            taskService.getTaskTitle(id);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving title for task ID: " + id, e);
        }
    }

    @KafkaListener(topics = "task-get-description-topic", groupId = "course-saga-group")
    public void getTaskDescriptionListener(String id) {
        try {
            taskService.getTaskDescription(id);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving description for task ID: " + id, e);
        }
    }

    @KafkaListener(topics = "task-get-video-url-topic", groupId = "course-saga-group")
    public void getTaskVideoUrlListener(String id) {
        try {
            taskService.getTaskVideoFileName(id);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving video URL for task ID: " + id, e);
        }
    }

    @KafkaListener(topics = "task-get-is-public-topic", groupId = "course-saga-group")
    public void getTaskIsPublicListener(String id) {
        try {
            taskService.getTaskIsPublic(id);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving task visibility for ID: " + id, e);
        }
    }

    @KafkaListener(topics = "task-get-hw-posting-interval-topic", groupId = "course-saga-group")
    public void getTaskHwPostingIntervalListener(String id) {
        try {
            taskService.getTaskHwPostingInterval(id);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving HW posting interval for task ID: " + id, e);
        }
    }

    @KafkaListener(topics = "task-get-prerequisites-topic", groupId = "course-saga-group")
    public void getTaskPrerequisitesListener(String id) {
        try {
            taskService.getTaskPrerequisites(id);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving prerequisites for task ID: " + id, e);
        }
    }

    @KafkaListener(topics = "task-get-successors-topic", groupId = "course-saga-group")
    public void getTaskSuccessorsListener(String id) {
        try {
            taskService.getTaskSuccessors(id);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving successors for task ID: " + id, e);
        }
    }
}