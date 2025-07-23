package com.jilnash.courseservicesaga.controller.module;

import com.jilnash.courseservicedto.dto.module.ModuleCreateDTO;
import com.jilnash.courseservicesaga.service.module.ModuleServiceSaga;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/modules")
public class ModuleServiceSagaController {

    private final ModuleServiceSaga moduleServiceSaga;

    public ModuleServiceSagaController(ModuleServiceSaga moduleServiceSaga) {
        this.moduleServiceSaga = moduleServiceSaga;
    }

    @PostMapping
    public ResponseEntity<?> createModule(@PathVariable String courseId,
                                          @RequestHeader("X-User-Sub") String authorId,
                                          @RequestBody @Validated ModuleCreateDTO moduleCreateDTO) {

        moduleCreateDTO.setCourseId(courseId);
        moduleCreateDTO.setAuthorId(authorId);
        moduleServiceSaga.createModule(moduleCreateDTO);

        return ResponseEntity.ok("Module created successfully");
    }

    @PatchMapping("/{moduleId}/name")
    public ResponseEntity<?> updateModuleName(@PathVariable String courseId,
                                              @PathVariable String moduleId,
                                              @RequestBody String newName) {

        moduleServiceSaga.updateModuleName(courseId, moduleId, newName);

        return ResponseEntity.ok("Module name updated successfully");
    }

    @PatchMapping("/{moduleId}/description")
    public ResponseEntity<?> updateModuleDescription(@PathVariable String courseId,
                                                     @PathVariable String moduleId,
                                                     @RequestBody String newDescription) {

        moduleServiceSaga.updateModuleDescription(courseId, moduleId, newDescription);

        return ResponseEntity.ok("Module description updated successfully");
    }

    @DeleteMapping("/{moduleId}/soft")
    public ResponseEntity<?> deleteModule(@PathVariable String courseId,
                                          @PathVariable String moduleId) {

        moduleServiceSaga.softDeleteModule(courseId, moduleId);

        return ResponseEntity.ok("Module soft deleted successfully");
    }

    @DeleteMapping("/{moduleId}/hard")
    public ResponseEntity<?> hardDeleteModule(@PathVariable String courseId,
                                              @PathVariable String moduleId) {

        moduleServiceSaga.hardDeleteModule(courseId, moduleId);

        return ResponseEntity.ok("Module hard deleted successfully");
    }
}
