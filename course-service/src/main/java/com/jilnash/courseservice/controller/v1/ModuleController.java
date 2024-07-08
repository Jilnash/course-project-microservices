package com.jilnash.courseservice.controller.v1;

import com.jilnash.courseservice.dto.AppResponse;
import com.jilnash.courseservice.dto.module.ModuleCreateDTO;
import com.jilnash.courseservice.dto.module.ModuleUpdateDTO;
import com.jilnash.courseservice.mapper.ModuleMapper;
import com.jilnash.courseservice.service.CourseService;
import com.jilnash.courseservice.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/modules")
public class ModuleController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ModuleMapper moduleMapper;

    @GetMapping
    public ResponseEntity<?> getModules(@PathVariable Long courseId,
                                        @RequestParam(required = false, defaultValue = "") String name) {

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Modules fetched successfully",
                        moduleService.getModules(name, courseId)
                )
        );
    }

    @PutMapping
    public ResponseEntity<?> createModule(@PathVariable Long courseId,
                                          @Validated @RequestBody ModuleCreateDTO moduleDTO) {

        //check if course exists, then set it
        moduleDTO.setCourseId(courseService.getCourse(courseId).getId());

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Module created successfully",
                        moduleService.saveModule(moduleMapper.toEntity(moduleDTO))
                )
        );
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<?> getModule(@PathVariable Long courseId,
                                       @PathVariable Long moduleId) {

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Module fetched successfully",
                        moduleService.getModule(moduleId, courseId)
                )
        );
    }

    @PostMapping("/{moduleId}")
    public ResponseEntity<?> updateModule(@PathVariable Long courseId,
                                          @PathVariable Long moduleId,
                                          @Validated @RequestBody ModuleUpdateDTO moduleDTO) {

        //check if module exists
        moduleService.getModule(moduleId, courseId);

        //then set it
        moduleDTO.setId(moduleId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Module updated successfully",
                        moduleService.saveModule(moduleMapper.toEntity(moduleDTO))
                )
        );
    }
}
