package com.jilnash.courseservicesaga.controller.task;

import com.jilnash.courseservicesaga.dto.TaskSagaCreateDTO;
import com.jilnash.courseservicesaga.service.task.TaskServiceSaga;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/modules/{moduleId}/tasks")
@Slf4j
public class TaskServiceSagaController {

    private final TaskServiceSaga taskServiceSaga;

    public TaskServiceSagaController(TaskServiceSaga taskServiceSaga) {
        this.taskServiceSaga = taskServiceSaga;
    }

    @GetMapping
    public ResponseEntity<?> getTasks(@PathVariable String courseId,
                                      @PathVariable String moduleId,
                                      @RequestHeader("X-User-Sub") String userId,
                                      @RequestParam(required = false, defaultValue = "") String name) {

        log.info("[CONTROLLER] Fetching tasks in course {} module {} with name {}", courseId, moduleId, name);

        taskServiceSaga.getTasks(userId, courseId, moduleId, name);
        return ResponseEntity.ok("Tasks fetched successfully");
    }

    @GetMapping("/graph")
    public ResponseEntity<?> getTasks(@PathVariable String courseId,
                                      @PathVariable String moduleId) {

        log.info("[CONTROLLER] Fetching task graph in course {} module {}", courseId, moduleId);

        return ResponseEntity.ok(taskServiceSaga.getTasksAsGraph(courseId, moduleId));
    }

    @PostMapping
    public ResponseEntity<?> createTask(@PathVariable String courseId,
                                        @PathVariable String moduleId,
                                        @RequestHeader("X-User-Sub") String teacherId,
                                        @ModelAttribute @Validated TaskSagaCreateDTO taskDto) {

        log.info("[CONTROLLER] Creating task in course {} module {}", courseId, moduleId);

        taskDto.setTaskId(UUID.randomUUID().toString());
        taskDto.setCourseId(courseId);
        taskDto.setModuleId(moduleId);
        taskDto.setAuthorId(teacherId);

        taskServiceSaga.createTask(taskDto);
        return ResponseEntity.ok("Task created successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable String courseId,
                                     @PathVariable String moduleId,
                                     @RequestHeader("X-User-Sub") String userId,
                                     @PathVariable String id) {

        log.info("[CONTROLLER] Fetching task in course {} module {} with id {}", courseId, moduleId, id);

        return ResponseEntity.ok(taskServiceSaga.getTask(userId, courseId, moduleId, id));
    }

    @PatchMapping("/{id}/title")
    public ResponseEntity<?> updateTaskTitle(@PathVariable String courseId,
                                             @PathVariable String moduleId,
                                             @PathVariable String id,
                                             @RequestHeader("X-User-Sub") String userId,
                                             @RequestBody String title) {

        log.info("[CONTROLLER] Updating task title in course {} module {} with id {}", courseId, moduleId, id);

        taskServiceSaga.updateTaskTitle(userId, courseId, moduleId, id, title);
        return ResponseEntity.ok("Task title updated successfully");
    }

    @PatchMapping("/{id}/description")
    public ResponseEntity<?> updateTaskDescription(@PathVariable String courseId,
                                                   @PathVariable String moduleId,
                                                   @PathVariable String id,
                                                   @RequestHeader("X-User-Sub") String userId,
                                                   @RequestBody String description) {

        log.info("[CONTROLLER] Updating task description in course {} module {} with id {}", courseId, moduleId, id);

        taskServiceSaga.updateTaskDescription(userId, courseId, moduleId, id, description);
        return ResponseEntity.ok("Task description updated successfully");
    }

    @PatchMapping("/{id}/video")
    public ResponseEntity<?> updateTaskVideo(@PathVariable String courseId,
                                             @PathVariable String moduleId,
                                             @PathVariable String id,
                                             @RequestHeader("X-User-Sub") String userId,
                                             @RequestBody MultipartFile videoFile) {

        log.info("[CONTROLLER] Updating task video in course {} module {} with id {}", courseId, moduleId, id);

        taskServiceSaga.updateTaskVideoFile(userId, courseId, moduleId, id, videoFile);
        return ResponseEntity.ok("Task video updated successfully");
    }

    @PatchMapping("/{id}/isPublic")
    public ResponseEntity<?> updateTaskIsPublic(@PathVariable String courseId,
                                                @PathVariable String moduleId,
                                                @PathVariable String id,
                                                @RequestHeader("X-User-Sub") String userId,
                                                @RequestBody Boolean isPublic) {

        log.info("[CONTROLLER] Updating task isPublic in course {} module {} with id {}", courseId, moduleId, id);

        taskServiceSaga.updateTaskIsPublic(userId, courseId, moduleId, id, isPublic);
        return ResponseEntity.ok("Task isPublic updated successfully");
    }

    @PatchMapping("/{id}/prerequisites")
    public ResponseEntity<?> updateTaskPrerequisite(@PathVariable String courseId,
                                                    @PathVariable String moduleId,
                                                    @PathVariable String id,
                                                    @RequestHeader("X-User-Sub") String userId,
                                                    @RequestBody Set<String> prerequisiteIds) {

        log.info("[CONTROLLER] Updating task prereqs in course {} module {} with id {}", courseId, moduleId, id);

        taskServiceSaga.updateTaskPrerequisites(userId, courseId, moduleId, id, prerequisiteIds);
        return ResponseEntity.ok("Task prereqs updated successfully");
    }

    @PatchMapping("/{id}/hwPostingInterval")
    public ResponseEntity<?> updateTaskHwPostingInterval(@PathVariable String courseId,
                                                         @PathVariable String moduleId,
                                                         @PathVariable String id,
                                                         @RequestHeader("X-User-Sub") String userId,
                                                         @RequestBody Integer hwPostingInterval) {

        log.info("[CONTROLLER] Updating task hwPostingInterval in course {} module {} with id {}", courseId, moduleId, id);

        taskServiceSaga.updateTaskHwPostingInterval(userId, courseId, moduleId, id, hwPostingInterval);
        return ResponseEntity.ok("Task hwPostingInterval updated successfully");
    }

    @PatchMapping("/{id}/successors")
    public ResponseEntity<?> updateTaskSuccessor(@PathVariable String courseId,
                                                 @PathVariable String moduleId,
                                                 @PathVariable String id,
                                                 @RequestHeader("X-User-Sub") String userId,
                                                 @RequestBody Set<String> successorIds) {

        log.info("[CONTROLLER] Updating task successors in course {} module {} with id {}", courseId, moduleId, id);

        taskServiceSaga.updateTaskSuccessors(userId, courseId, moduleId, id, successorIds);
        return ResponseEntity.ok("Task successors updated successfully");
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<?> softDeleteTask(@PathVariable String courseId,
                                            @PathVariable String moduleId,
                                            @RequestHeader("X-User-Sub") String userId,
                                            @PathVariable String id) {

        log.info("[CONTROLLER] Soft deleting task in course {} module {} with id {}", courseId, moduleId, id);

        taskServiceSaga.softDeleteTask(userId, courseId, moduleId, id);
        return ResponseEntity.ok("Task soft deleted successfully");
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<?> hardDeleteTask(@PathVariable String courseId,
                                            @PathVariable String moduleId,
                                            @RequestHeader("X-User-Sub") String userId,
                                            @PathVariable String id) {

        log.info("[CONTROLLER] Hard deleting task in course {} module {} with id {}", courseId, moduleId, id);

        taskServiceSaga.hardDeleteTask(userId, courseId, moduleId, id);
        return ResponseEntity.ok("Task hard deleted successfully");
    }
}
