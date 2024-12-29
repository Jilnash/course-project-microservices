package com.jilnash.courserightsservice.controller.v1;

import com.jilnash.courserightsservice.service.TeacherRightsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/v1/courses/{courseId}/teachers/{teacherId}/rights")
@RequiredArgsConstructor
public class RightsController {

    private final TeacherRightsService teacherRightsService;

    @GetMapping
    public Set<String> getRights(@PathVariable String teacherId, @PathVariable String courseId) {
        return teacherRightsService.getRights(courseId, teacherId);
    }

    @PostMapping
    public ResponseEntity<?> setRights(@PathVariable String teacherId,
                                    @PathVariable String courseId,
                                       @RequestBody Set<String> rights) {
        return ResponseEntity.ok(
                teacherRightsService.setRights(courseId, teacherId, rights)
        );
    }

    @GetMapping("/has")
    public Boolean hasRights(@PathVariable String teacherId,
                             @PathVariable String courseId,
                             @RequestParam Set<String> rights) {
        return teacherRightsService.hasRights(courseId, teacherId, rights);
    }
}
