package com.jilnash.courseservice.controller.v1;

import com.jilnash.courseservice.dto.AppResponse;
import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.mapper.TaskMapper;
import com.jilnash.courseservice.service.task.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/v1/courses/{courseId}/modules/{moduleId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskServiceImpl taskService;
    private final TaskMapper taskMapper;

    @GetMapping
    public ResponseEntity<?> getTasks(@PathVariable String courseId,
                                      @PathVariable String moduleId,
                                      @RequestParam(required = false, defaultValue = "") String name) {

        log.info("[CONTROLLER] Fetching tasks in course {} module {} with name {}", courseId, moduleId, name);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Tasks fetched successfully",
                        taskService.getTasks(courseId, moduleId, name)
                )
        );
    }

    @GetMapping("/graph")
    public ResponseEntity<?> getTasks(@PathVariable String courseId,
                                      @PathVariable String moduleId) {

        log.info("[CONTROLLER] Fetching task graph in course {} module {}", courseId, moduleId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Tasks fetched successfully",
                        taskService.getTasksAsGraph(courseId, moduleId)
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> createTask(@PathVariable String courseId,
                                        @PathVariable String moduleId,
                                        @RequestHeader("X-User-Sub") String teacherId,
                                        @RequestBody @Validated TaskCreateDTO taskDto) {

        log.info("[CONTROLLER] Creating task in course {} module {}", courseId, moduleId);

        taskDto.setCourseId(courseId);
        taskDto.setModuleId(moduleId);
        taskDto.setTeacherId(teacherId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task created successfully",
                        taskMapper.toTaskCreateResponse(taskService.createTask(taskDto))
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable String courseId,
                                     @PathVariable String moduleId,
                                     @PathVariable String id) {

        log.info("[CONTROLLER] Fetching task in course {} module {} with id {}", courseId, moduleId, id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task fetched successfully",
                        taskMapper.toTaskResponse(taskService.getTask(courseId, moduleId, id))
                )
        );
    }

    @PatchMapping("/{id}/title")
    public ResponseEntity<?> updateTaskTitle(@PathVariable String courseId,
                                             @PathVariable String moduleId,
                                             @PathVariable String id,
                                             @RequestBody String title) {

        log.info("[CONTROLLER] Updating task title in course {} module {} with id {}", courseId, moduleId, id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task title updated successfully",
                        taskService.updateTaskTitle(courseId, moduleId, id, title)
                )
        );
    }

    @PatchMapping("/{id}/description")
    public ResponseEntity<?> updateTaskDescription(@PathVariable String courseId,
                                                   @PathVariable String moduleId,
                                                   @PathVariable String id,
                                                   @RequestBody String description) {

        log.info("[CONTROLLER] Updating task description in course {} module {} with id {}", courseId, moduleId, id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task description updated successfully",
                        taskService.updateTaskDescription(courseId, moduleId, id, description)
                )
        );
    }

    @PatchMapping("/{id}/video")
    public ResponseEntity<?> updateTaskVideo(@PathVariable String courseId,
                                             @PathVariable String moduleId,
                                             @PathVariable String id,
                                             @RequestBody String videoFileName) {

        log.info("[CONTROLLER] Updating task video in course {} module {} with id {}", courseId, moduleId, id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task video updated successfully",
                        taskService.updateTaskVideoFileName(courseId, moduleId, id, videoFileName)
                )
        );
    }

    @PatchMapping("/{id}/isPublic")
    public ResponseEntity<?> updateTaskIsPublic(@PathVariable String courseId,
                                                @PathVariable String moduleId,
                                                @PathVariable String id,
                                                @RequestBody Boolean isPublic) {

        log.info("[CONTROLLER] Updating task isPublic in course {} module {} with id {}", courseId, moduleId, id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task isPublic updated successfully",
                        taskService.updateTaskIsPublic(courseId, moduleId, id, isPublic)
                )
        );
    }

    @PatchMapping("/{id}/prerequisites")
    public ResponseEntity<?> updateTaskPrerequisite(@PathVariable String courseId,
                                                    @PathVariable String moduleId,
                                                    @PathVariable String id,
                                                    @RequestBody Set<String> prerequisiteIds) {

        log.info("[CONTROLLER] Updating task prereqs in course {} module {} with id {}", courseId, moduleId, id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task prereqs updated successfully",
                        taskService.updateTaskPrerequisites(courseId, moduleId, id, prerequisiteIds)
                )
        );
    }

    @PatchMapping("/{id}/hwPostingInterval")
    public ResponseEntity<?> updateTaskHwPostingInterval(@PathVariable String courseId,
                                                         @PathVariable String moduleId,
                                                         @PathVariable String id,
                                                         @RequestBody Integer hwPostingInterval) {

        log.info("[CONTROLLER] Updating task hwPostingInterval in course {} module {} with id {}", courseId, moduleId, id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task hwPostingInterval updated successfully",
                        taskService.updateTaskHwPostingInterval(courseId, moduleId, id, hwPostingInterval)
                )
        );
    }

    @PatchMapping("/{id}/successors")
    public ResponseEntity<?> updateTaskSuccessor(@PathVariable String courseId,
                                                 @PathVariable String moduleId,
                                                 @PathVariable String id,
                                                 @RequestBody Set<String> successorIds) {

        log.info("[CONTROLLER] Updating task successors in course {} module {} with id {}", courseId, moduleId, id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task successors updated successfully",
                        taskService.updateTaskSuccessors(courseId, moduleId, id, successorIds)
                )
        );
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<?> softDeleteTask(@PathVariable String courseId,
                                            @PathVariable String moduleId,
                                            @PathVariable String id) {

        log.info("[CONTROLLER] Soft deleting task in course {} module {} with id {}", courseId, moduleId, id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task soft deleted successfully",
                        taskService.softDeleteTask(courseId, moduleId, id)
                )
        );
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<?> hardDeleteTask(@PathVariable String courseId,
                                            @PathVariable String moduleId,
                                            @PathVariable String id) {

        log.info("[CONTROLLER] Hard deleting task in course {} module {} with id {}", courseId, moduleId, id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task hard deleted successfully",
                        taskService.hardDeleteTask(courseId, moduleId, id)
                )
        );
    }
}
