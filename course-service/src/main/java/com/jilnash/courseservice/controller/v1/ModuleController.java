package com.jilnash.courseservice.controller.v1;

import com.jilnash.courseservice.dto.AppResponse;
import com.jilnash.courseservice.dto.module.ModuleCreateDTO;
import com.jilnash.courseservice.dto.module.ModuleUpdateDTO;
import com.jilnash.courseservice.service.module.AuthorizedModuleService;
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

    private final AuthorizedModuleService moduleService;

    @GetMapping
    public ResponseEntity<?> getModules(@PathVariable String courseId) {

        log.info("[CONTROLLER] Fetching modules in course: {}", courseId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Modules fetched successfully",
                        moduleService.getModulesInCourse(courseId)
                )
        );
    }

    @PutMapping
    public ResponseEntity<?> createModule(@PathVariable String courseId,
                                          @RequestHeader("X-User-Sub") String teacherId,
                                          @Validated @RequestBody ModuleCreateDTO moduleCreateDTO) {

        log.info("[CONTROLLER] Creating module in course: {}", courseId);

        moduleCreateDTO.setCourseId(courseId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Modules created successfully",
                        moduleService.createModuleByUser(teacherId, moduleCreateDTO)

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
                        moduleService.getModuleByCourse(courseId, moduleId)

                )
        );
    }

    @PostMapping("{moduleId}")
    public ResponseEntity<?> updateModule(@PathVariable String courseId,
                                          @PathVariable String moduleId,
                                          @Validated @RequestBody ModuleUpdateDTO moduleUpdateDTO,
                                          @RequestHeader("X-User-Sub") String teacherId) {

        log.info("[CONTROLLER] Updating module in course: {}", courseId);

        moduleUpdateDTO.setCourseId(courseId);
        moduleUpdateDTO.setId(moduleId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Module updated successfully",
                        moduleService.updateModuleByUser(teacherId, moduleUpdateDTO)
                )
        );
    }

}
