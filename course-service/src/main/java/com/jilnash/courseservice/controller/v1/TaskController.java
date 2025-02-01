package com.jilnash.courseservice.controller.v1;

import com.jilnash.courseservice.dto.AppResponse;
import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.dto.task.TaskUpdateDTO;
import com.jilnash.courseservice.service.task.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/modules/{moduleId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskServiceImpl taskService;

    @GetMapping
    public ResponseEntity<?> getTasks(@PathVariable String courseId,
                                      @PathVariable String moduleId,
                                      @RequestParam String mode,
                                      @RequestParam(required = false, defaultValue = "") String name) {

        if (mode.equals("list"))
            return ResponseEntity.ok(
                    new AppResponse(
                            200,
                            "Tasks fetched successfully",
                            taskService.getTasks(courseId, moduleId, name)
                    )
            );

        if (mode.equals("graph"))
            return ResponseEntity.ok(
                    new AppResponse(
                            200,
                            "Tasks fetched successfully",
                            taskService.getTaskGraph(courseId, moduleId)
                    )
            );

        return ResponseEntity.badRequest().body(
                new AppResponse(
                        400,
                        "Invalid mode",
                        null
                )
        );
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createTask(@PathVariable String courseId,
                                        @PathVariable String moduleId,
                                        @ModelAttribute @Validated TaskCreateDTO taskDto,
                                        @RequestHeader("X-User-Sub") String teacherId) {

        taskDto.setCourseId(courseId);
        taskDto.setModuleId(moduleId);
        taskDto.setTeacherId(teacherId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task created successfully",
                        taskService.create(taskDto)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable String courseId,
                                     @PathVariable String moduleId,
                                     @PathVariable String id,
                                     @RequestHeader("X-User-Sub") String userId) {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task fetched successfully",
                        taskService.getTaskToUser(userId, courseId, moduleId, id)
                )
        );
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable String courseId,
                                        @PathVariable String moduleId,
                                        @PathVariable String id,
                                        @RequestBody TaskUpdateDTO taskDto) {

        taskDto.setId(id);
        taskDto.setCourseId(courseId);
        taskDto.setModuleId(moduleId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task updated successfully",
                        taskService.update(taskDto)
                )
        );
    }

    @GetMapping("/{id}/prerequisites")
    public ResponseEntity<?> getTaskPrerequisites(@PathVariable String courseId,
                                                  @PathVariable String moduleId,
                                                  @PathVariable String id) {

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task prereqs fetched successfully",
                        taskService.getTaskPrerequisites(courseId, moduleId, id)
                )
        );
    }

    @PostMapping("/{id}/prerequisites")
    public ResponseEntity<?> updateTaskPrerequisite(@PathVariable String courseId,
                                                    @PathVariable String moduleId,
                                                    @PathVariable String id,
                                                    @RequestBody Set<String> prerequisiteIds) {

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Task prereqs updated successfully",
                        taskService.updateTaskPrerequisite(courseId, moduleId, id, prerequisiteIds)
                )
        );
    }
}
