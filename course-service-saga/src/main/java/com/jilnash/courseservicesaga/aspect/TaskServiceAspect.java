package com.jilnash.courseservicesaga.aspect;

import com.jilnash.courseservicedto.dto.task.*;
import com.jilnash.courseservicesaga.dto.TaskSagaCreateDTO;
import com.jilnash.courseservicesaga.transaction.RollbackStage;
import com.jilnash.courseservicesaga.transaction.Transaction;
import com.jilnash.taskrequirementsservicedto.dto.SetRequirements;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Aspect
@Component
public class TaskServiceAspect {

    private final Map<String, Transaction> transactionMap;

    private final HttpServletRequest request;

    public TaskServiceAspect(Map<String, Transaction> transactionMap, HttpServletRequest request) {
        this.transactionMap = transactionMap;
        this.request = request;
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.createTask(..))" +
                    "&& args(dto)",
            argNames = "dto"
    )
    public void beforeCreateTask(TaskSagaCreateDTO dto) {

        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-create-rollback-topic", dto),
                new RollbackStage("insert-task-progress-rollback-topic", dto.getTaskId()),
                new RollbackStage("set-task-requirements-rollback-topic",
                        new SetRequirements(transactionId, dto.getTaskId(), dto.getReqirements()))
                //todo: file upload rollback
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.updateTaskTitle(..))" +
                    "&& args(teacherId, courseId, moduleId, taskId, title)",
            argNames = "teacherId, courseId, moduleId, taskId, title"
    )
    public void beforeUpdateTaskTitle(String teacherId, String courseId, String moduleId, String taskId, String title) {

        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-update-title-rollback-topic",
                        new TaskUpdateTitleDTO(courseId, moduleId, taskId, "Prev title"))
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.updateTaskDescription(..))" +
                    "&& args(teacherId, courseId, moduleId, taskId, description)",
            argNames = "teacherId, courseId, moduleId, taskId, description"
    )
    public void beforeUpdateTaskDescription(String teacherId, String courseId, String moduleId, String taskId, String description) {

        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-update-description-rollback-topic",
                        new TaskUpdateDescriptionDTO(courseId, moduleId, taskId, "Prev description"))
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.updateTaskVideoFile(..))" +
                    "&& args(teacherId, courseId, moduleId, taskId, videoFile)",
            argNames = "teacherId, courseId, moduleId, taskId, videoFile"
    )
    public void beforeUpdateTaskVideoFileName(String teacherId, String courseId, String moduleId, String taskId, MultipartFile videoFile) {

        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-update-video-file-name-rollback-topic",
                        new TaskUpdateVideoFileDTO(courseId, moduleId, taskId, "prev file"))
                //todo: rollback file update
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.updateTaskIsPublic(..))" +
                    "&& args(teacherId, courseId, moduleId, taskId, isPublic)",
            argNames = "teacherId, courseId, moduleId, taskId, isPublic"
    )
    public void beforeUpdateTaskIsPublic(String teacherId, String courseId, String moduleId, String taskId, Boolean isPublic) {
        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-update-is-public-rollback-topic",
                        new TaskUpdateIsPublicDTO(courseId, moduleId, taskId, false))
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.updateTaskHwPostingInterval(..))" +
                    "&& args(teacherId, courseId, moduleId, taskId, hwPostingInterval)",
            argNames = "teacherId, courseId, moduleId, taskId, hwPostingInterval"
    )
    public void beforeUpdateTaskHwPostingInterval(String teacherId, String courseId, String moduleId, String taskId, Integer hwPostingInterval) {

        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-update-hw-posting-interval-rollback-topic",
                        new TaskUpdateHwIntervalDTO(courseId, moduleId, taskId, 0))
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.updateTaskPrerequisites(..))" +
                    "&& args(teacherId, courseId, moduleId, taskId, prerequisiteTaskIds)",
            argNames = "teacherId, courseId, moduleId, taskId, prerequisiteTaskIds"
    )
    public void beforeUpdateTaskPrerequisites(String teacherId, String courseId, String moduleId, String taskId, Set<String> prerequisiteTaskIds) {

        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-update-prerequisites-rollback-topic",
                        new TaskUpdatePrereqsDTO(courseId, moduleId, taskId, Set.of()))
                //todo: rollback progress update
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.updateTaskSuccessors(..))" +
                    "&& args(teacherId, courseId, moduleId, taskId, successorTasksIds)",
            argNames = "teacherId, courseId, moduleId, taskId, successorTasksIds"
    )
    public void beforeUpdateTaskSuccessors(String teacherId, String courseId, String moduleId, String taskId, Set<String> successorTasksIds) {
        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-update-successors-rollback-topic",
                        new TaskUpdateSuccessorsDTO(courseId, moduleId, taskId, Set.of()))
                //todo: rollback progress update
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

    @Before(
            value = "execution(* com.jilnash.courseservicesaga.service.task.TaskServiceSagaImpl.softDeleteTask(..))" +
                    "&& args(teacherId, courseId, moduleId, taskId)",
            argNames = "teacherId, courseId, moduleId, taskId"
    )
    public void beforeSoftDeleteTask(String teacherId, String courseId, String moduleId, String taskId) {

        String transactionId = request.getHeader("X-Transaction-Id");

        List<RollbackStage> rollbackStages = List.of(
                new RollbackStage("task-soft-delete-rollback-topic",
                        new TaskDeleteDTO(courseId, moduleId, taskId)),
                new RollbackStage("soft-delete-progress-rollback-topic", List.of(taskId))
        );

        transactionMap.putIfAbsent(transactionId, new Transaction(transactionId, rollbackStages));
    }

}
