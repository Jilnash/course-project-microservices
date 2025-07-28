package com.jilnash.courseservicesaga.controller.task;

import com.jilnash.courseservicesaga.service.task.TaskServiceRollbackSaga;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/modules/{moduleId}/tasks/{taskId}")
public class TaskServiceSagaRollbackController {

    private final TaskServiceRollbackSaga taskServiceRollbackSaga;

    public TaskServiceSagaRollbackController(TaskServiceRollbackSaga taskServiceRollbackSaga) {
        this.taskServiceRollbackSaga = taskServiceRollbackSaga;
    }

    @PostMapping("rollback")
    public ResponseEntity<?> rollbackTaskCreate(@PathVariable String courseId,
                                                @PathVariable String moduleId,
                                                @PathVariable String taskId) {

        taskServiceRollbackSaga.rollbackTaskCreate(courseId, moduleId, taskId);
        return ResponseEntity.ok("Task creation rollback initiated successfully");
    }

    @PatchMapping("title/rollback")
    public ResponseEntity<?> rollbackTaskTitleUpdate(@PathVariable String courseId,
                                                     @PathVariable String moduleId,
                                                     @PathVariable String taskId,
                                                     @RequestBody String oldTitle) {

        taskServiceRollbackSaga.rollbackTaskTitleUpdate(courseId, moduleId, taskId, oldTitle);
        return ResponseEntity.ok("Task title update rollback initiated successfully");
    }

    @PatchMapping("description/rollback")
    public ResponseEntity<?> rollbackTaskDescriptionUpdate(@PathVariable String courseId,
                                                           @PathVariable String moduleId,
                                                           @PathVariable String taskId,
                                                           @RequestBody String oldDescription) {

        taskServiceRollbackSaga.rollbackTaskDescriptionUpdate(courseId, moduleId, taskId, oldDescription);
        return ResponseEntity.ok("Task description update rollback initiated successfully");
    }

    @PatchMapping("videofile/rollback")
    public ResponseEntity<?> rollbackTaskVideoFileUpdate(@PathVariable String courseId,
                                                         @PathVariable String moduleId,
                                                         @PathVariable String taskId,
                                                         @ModelAttribute MultipartFile oldFile) {

        taskServiceRollbackSaga.rollbackTaskVideoFileUpdate(courseId, moduleId, taskId, oldFile);
        return ResponseEntity.ok("Task video file name update rollback initiated successfully");
    }

    @PatchMapping("is-public/rollback")
    public ResponseEntity<?> rollbackTaskIsPublicUpdate(@PathVariable String courseId,
                                                        @PathVariable String moduleId,
                                                        @PathVariable String taskId,
                                                        @RequestBody boolean oldIsPublic) {

        taskServiceRollbackSaga.rollbackTaskIsPublicUpdate(courseId, moduleId, taskId, oldIsPublic);
        return ResponseEntity.ok("Task is public update rollback initiated successfully");
    }

    @PatchMapping("prerequisites/rollback")
    public ResponseEntity<?> rollbackTaskPrerequisitesUpdate(@PathVariable String courseId,
                                                             @PathVariable String moduleId,
                                                             @PathVariable String taskId,
                                                             @RequestBody Set<String> oldPrerequisites) {

        taskServiceRollbackSaga.rollbackTaskPrerequisitesUpdate(courseId, moduleId, taskId, oldPrerequisites);
        return ResponseEntity.ok("Task prerequisites update rollback initiated successfully");
    }

    @PatchMapping("successors/rollback")
    public ResponseEntity<?> rollbackTaskSuccessorsUpdate(@PathVariable String courseId,
                                                          @PathVariable String moduleId,
                                                          @PathVariable String taskId,
                                                          @RequestBody Set<String> oldSuccessors) {

        taskServiceRollbackSaga.rollbackTaskSuccessorsUpdate(courseId, moduleId, taskId, oldSuccessors);
        return ResponseEntity.ok("Task successors update rollback initiated successfully");
    }

    @DeleteMapping("soft/rollback")
    public ResponseEntity<?> rollbackTaskSoftDelete(@PathVariable String courseId,
                                                    @PathVariable String moduleId,
                                                    @PathVariable String taskId) {

        taskServiceRollbackSaga.rollbackTaskSoftDelete(courseId, moduleId, taskId);
        return ResponseEntity.ok("Task soft delete rollback initiated successfully");
    }
}
