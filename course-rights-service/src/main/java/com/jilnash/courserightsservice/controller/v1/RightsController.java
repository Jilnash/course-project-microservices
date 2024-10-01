package com.jilnash.courserightsservice.controller.v1;

import com.jilnash.courserightsservice.service.TeacherRightsService;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/courses/{courseId}/teachers/{teacherId}/rights")
public class RightsController {

    private final String rights = "edit|delete|add|manageTeachers";
    private final String message = "Rights must be one of: edit, delete, add, manageTeachers";

    private final TeacherRightsService teacherRightsService;

    public RightsController(TeacherRightsService teacherRightsService) {
        this.teacherRightsService = teacherRightsService;
    }

    @GetMapping
    public List<String> getRights(@PathVariable String teacherId, @PathVariable String courseId) {
        return teacherRightsService.getRights(courseId, teacherId);
    }

    @PostMapping
    public ResponseEntity<?> create(@PathVariable String teacherId,
                                    @PathVariable String courseId,
                                    @RequestParam @Validated List<@Pattern(regexp = rights, message = message) String> rights) {
        return ResponseEntity.ok(
                teacherRightsService.create(courseId, teacherId, rights)
        );
    }

    @PutMapping
    public ResponseEntity<?> update(@PathVariable String teacherId,
                                    @PathVariable String courseId,
                                    @RequestParam @Validated List<@Pattern(regexp = rights, message = message) String> rights) {
        return ResponseEntity.ok(
                teacherRightsService.update(courseId, teacherId, rights)
        );
    }

    @GetMapping("/has")
    public Boolean hasRights(@PathVariable String teacherId,
                             @PathVariable String courseId,
                             @RequestParam @Validated List<@Pattern(regexp = rights, message = message) String> rights) {
        return teacherRightsService.hasRights(courseId, teacherId, rights);
    }
}
