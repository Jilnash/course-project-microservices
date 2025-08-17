package com.jilnash.courseservicesaga.aspect;

import com.jilnash.courseservicedto.dto.task.TaskRollbackDTO;
import com.jilnash.courseservicesaga.dto.TaskSagaCreateDTO;
import com.jilnash.courseservicesaga.mapper.TaskMapper;
import com.jilnash.courseservicesaga.transaction.RollbackStage;
import com.jilnash.courseservicesaga.transaction.Transaction;
import com.jilnash.fileservicedto.dto.FileUpdateRollbackDTO;
import com.jilnash.fileservicedto.dto.FileUploadRollbackDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class TaskServiceAspect {

    private final Map<String, Transaction> transactionMap;

    private final TaskMapper taskMapper;

    private final HttpServletRequest request;

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.createTask(..))" +
                    "&& args(dto)",
            argNames = "dto"
    )
    public void beforeCreateTask(TaskSagaCreateDTO dto) {

        String transactionId = request.getHeader("X-Transaction-Id");
        String fileName = "task-" + dto.getTaskId() + "/" + dto.getVideoFile().getOriginalFilename();

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-create-rollback-topic", taskMapper.toTaskCreateDTO(dto)),
                new RollbackStage("insert-task-progress-rollback-topic", dto.getTaskId()),
                new RollbackStage("create-task-requirements-rollback-topic", dto.getTaskId()),
                new RollbackStage("file-upload-rollback-topic",
                        new FileUploadRollbackDTO(transactionId, "course-project-tasks", List.of(fileName)))
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.updateTaskTitle(..))" +
                    "&& args(*, courseId, moduleId, taskId, ..)",
            argNames = "courseId, moduleId, taskId"
    )
    public void beforeUpdateTaskTitle(String courseId, String moduleId, String taskId) {

        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-update-title-rollback-topic", new TaskRollbackDTO(courseId, moduleId, taskId))
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.updateTaskDescription(..))" +
                    "&& args(*, courseId, moduleId, taskId, ..)",
            argNames = "courseId, moduleId, taskId"
    )
    public void beforeUpdateTaskDescription(String courseId, String moduleId, String taskId) {

        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-update-description-rollback-topic", new TaskRollbackDTO(courseId, moduleId, taskId))
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.updateTaskVideoFile(..))" +
                    "&& args(*, courseId, moduleId, taskId, ..)",
            argNames = "courseId, moduleId, taskId"
    )
    public void beforeUpdateTaskVideoFileName(String courseId, String moduleId, String taskId) {

        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-update-video-file-name-rollback-topic", new TaskRollbackDTO(courseId, moduleId, taskId)),
                new RollbackStage("file-update-rollback-topic",
                        new FileUpdateRollbackDTO(transactionId, "course-project-tasks-deleted",
                                "course-project-tasks", "task-" + taskId))
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.updateTaskIsPublic(..))" +
                    "&& args(*, courseId, moduleId, taskId, ..)",
            argNames = "courseId, moduleId, taskId"
    )
    public void beforeUpdateTaskIsPublic(String courseId, String moduleId, String taskId) {
        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-update-is-public-rollback-topic", new TaskRollbackDTO(courseId, moduleId, taskId))
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.updateTaskHwPostingInterval(..))" +
                    "&& args(*, courseId, moduleId, taskId, ..)",
            argNames = "courseId, moduleId, taskId"
    )
    public void beforeUpdateTaskHwPostingInterval(String courseId, String moduleId, String taskId) {

        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-update-hw-posting-interval-rollback-topic", new TaskRollbackDTO(courseId, moduleId, taskId))
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.updateTaskPrerequisites(..))" +
                    "&& args(*, *, *,  taskId, ..)",
            argNames = "taskId"
    )
    public void beforeUpdateTaskPrerequisites(String taskId) {

        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-update-prerequisites-rollback-topic", taskId)
                //todo: rollback progress update
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.updateTaskSuccessors(..))" +
                    "&& args(*, *, *, taskId, ..)",
            argNames = "taskId"
    )
    public void beforeUpdateTaskSuccessors(String taskId) {
        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-update-successors-rollback-topic", taskId)
                //todo: rollback progress update
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.softDeleteTask(..))" +
                    "&& args(*, courseId, moduleId, taskId)",
            argNames = "courseId, moduleId, taskId"
    )
    public void beforeSoftDeleteTask(String courseId, String moduleId, String taskId) {

        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-soft-delete-rollback-topic", new TaskRollbackDTO(courseId, moduleId, taskId)),
                new RollbackStage("soft-delete-progress-rollback-topic", List.of(taskId))
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

}
