package com.jilnash.courseservice.controller.v1;

import com.jilnash.courseservice.dto.AppResponse;
import com.jilnash.courseservice.dto.module.ModuleCreateDTO;
import com.jilnash.courseservice.dto.module.ModuleUpdateDTO;
import com.jilnash.courseservice.service.module.ModuleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleServiceImpl moduleService;

    @GetMapping
    public ResponseEntity<?> getModules(@PathVariable String courseId,
                                        @RequestParam(required = false, defaultValue = "") String name) {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Modules fetched successfully",
                        moduleService.getModules(courseId, name)

                )
        );
    }

    @PutMapping
    public ResponseEntity<?> createModule(@PathVariable String courseId,
                                          @RequestHeader("X-User-Sub") String teacherId,
                                          @Validated @RequestBody ModuleCreateDTO moduleCreateDTO) {

        moduleCreateDTO.setCourseId(courseId);
        moduleCreateDTO.setTeacherId(teacherId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Modules created successfully",
                        moduleService.create(moduleCreateDTO)

                )
        );
    }


    @GetMapping("/{moduleId}")
    public ResponseEntity<?> getModule(@PathVariable String courseId,
                                       @PathVariable String moduleId) {
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

        moduleUpdateDTO.setCourseId(courseId);
        moduleUpdateDTO.setTeacherId(teacherId);
        moduleUpdateDTO.setId(moduleId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Module updated successfully",
                        moduleService.update(moduleUpdateDTO)
                )
        );
    }

}
