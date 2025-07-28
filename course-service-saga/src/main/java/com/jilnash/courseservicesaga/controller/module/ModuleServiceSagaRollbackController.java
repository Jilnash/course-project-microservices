package com.jilnash.courseservicesaga.controller.module;

import com.jilnash.courseservicesaga.service.module.ModuleServiceRollbackSaga;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/modules/{moduleId}/rollback")
public class ModuleServiceSagaRollbackController {

    private final ModuleServiceRollbackSaga moduleServiceRollbackSaga;

    public ModuleServiceSagaRollbackController(ModuleServiceRollbackSaga moduleServiceRollbackSaga) {
        this.moduleServiceRollbackSaga = moduleServiceRollbackSaga;
    }

    @PostMapping
    public ResponseEntity<?> createModuleRollback(@PathVariable String moduleId) {

        moduleServiceRollbackSaga.createModuleRollback(moduleId);
        return ResponseEntity.ok("Module created successfully");
    }

    @PatchMapping("/name")
    public ResponseEntity<?> updateModuleNameRollback(@PathVariable String courseId,
                                                      @PathVariable String moduleId,
                                                      @RequestBody String prevName) {

        moduleServiceRollbackSaga.updateModuleNameRollback(courseId, moduleId, prevName);
        return ResponseEntity.ok("Module name updated successfully");
    }

    @PatchMapping("/description")
    public ResponseEntity<?> updateModuleDescriptionRollback(@PathVariable String courseId,
                                                             @PathVariable String moduleId,
                                                             @RequestBody String description) {

        moduleServiceRollbackSaga.updateModuleDescriptionRollback(courseId, moduleId, description);
        return ResponseEntity.ok("Module description updated successfully");
    }

    @DeleteMapping("/soft")
    public ResponseEntity<?> deleteModuleRollback(@PathVariable String courseId,
                                                  @PathVariable String moduleId) {

        moduleServiceRollbackSaga.softDeleteModuleRollback(courseId, moduleId);
        return ResponseEntity.ok("Module soft deleted successfully");
    }
}
