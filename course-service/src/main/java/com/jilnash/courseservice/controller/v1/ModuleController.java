package com.jilnash.courseservice.controller.v1;

import com.jilnash.courseservice.dto.AppResponse;
import com.jilnash.courseservice.dto.module.ModuleCreateDTO;
import com.jilnash.courseservice.service.module.ModuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/courses/{courseId}/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @GetMapping
    public ResponseEntity<?> getModules(@PathVariable String courseId) {

        log.info("[CONTROLLER] Fetching modules in course: {}", courseId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Modules fetched successfully",
                        moduleService.getModules(courseId)
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> createModule(@PathVariable String courseId,
//                                          @RequestHeader("X-User-Sub") String teacherId,
                                          @Validated @RequestBody ModuleCreateDTO moduleCreateDTO) {

        log.info("[CONTROLLER] Creating module in course: {}", courseId);

        moduleCreateDTO.setCourseId(courseId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Modules created successfully",
                        moduleService.createModule(moduleCreateDTO)

                )
        );
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<?> getModule(@PathVariable String courseId,
                                       @PathVariable String moduleId) {

        log.info("[CONTROLLER] Fetching module in course: {}", courseId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Module fetched successfully",
                        moduleService.getModule(courseId, moduleId)

                )
        );
    }

    @PatchMapping("{moduleId}/name")
    public ResponseEntity<?> updateModuleName(@PathVariable String courseId,
                                              @PathVariable String moduleId,
                                              @Validated @RequestBody String newName) {

        log.info("[CONTROLLER] Updating module in course: {}", courseId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Module updated successfully",
                        moduleService.updateModuleName(courseId, moduleId, newName)
                )
        );
    }

    @PatchMapping("{moduleId}/description")
    public ResponseEntity<?> updateModuleDescription(@PathVariable String courseId,
                                                     @PathVariable String moduleId,
                                                     @Validated @RequestBody String newDescription) {

        log.info("[CONTROLLER] Updating module description in course: {}", courseId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Module description updated successfully",
                        moduleService.updateModuleDescription(courseId, moduleId, newDescription)
                )
        );
    }

    @DeleteMapping("/{moduleId}/soft")
    public ResponseEntity<?> softDeleteModule(@PathVariable String courseId,
                                              @PathVariable String moduleId) {

        log.info("[CONTROLLER] Soft deleting module in course: {}", courseId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Module soft deleted successfully",
                        moduleService.softDeleteModule(moduleId)
                )
        );
    }

    @DeleteMapping("/{moduleId}/hard")
    public ResponseEntity<?> hardDeleteModule(@PathVariable String courseId,
                                              @PathVariable String moduleId) {

        log.info("[CONTROLLER] Hard deleting module in course: {}", courseId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Module hard deleted successfully",
                        moduleService.hardDeleteModule(moduleId)
                )
        );
    }
}
