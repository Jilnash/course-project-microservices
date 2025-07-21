package com.jilnash.courseservicesaga.controller.module;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/modules/{moduleId}/rollback")
public class ModuleServiceSagaRollbackController {

    @PutMapping
    public ResponseEntity<?> createModuleRollback(@PathVariable String courseId) {
        return ResponseEntity.ok("Module created successfully");
    }

    @PutMapping("/name")
    public ResponseEntity<?> updateModuleNameRollback(@PathVariable String courseId,
                                                      @PathVariable String moduleId) {
        return ResponseEntity.ok("Module name updated successfully");
    }

    @PutMapping("/description")
    public ResponseEntity<?> updateModuleDescriptionRollback(@PathVariable String courseId,
                                                             @PathVariable String moduleId) {
        return ResponseEntity.ok("Module description updated successfully");
    }

    @DeleteMapping("/soft")
    public ResponseEntity<?> deleteModuleRollback(@PathVariable String courseId,
                                                  @PathVariable String moduleId) {
        return ResponseEntity.ok("Module soft deleted successfully");
    }
}
