package com.jilnash.courserightsservice.controller.v1;

import com.jilnash.courserightsservice.service.TeacherRightsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("api/v1/courses/{courseId}/teachers/{teacherId}/rights")
@RequiredArgsConstructor
public class RightsController {

    private final TeacherRightsService teacherRightsService;

    @GetMapping
    public Set<String> getRights(@PathVariable String teacherId, @PathVariable String courseId) {

        log.info("[CONTROLLER] Fetching teacher rights");
        log.debug("[CONTROLLER] Fetching teacher rights for teacher: {}, in course: {}", teacherId, courseId);

        return teacherRightsService.getRights(courseId, teacherId);
    }

    @PostMapping
    public ResponseEntity<?> setRights(@PathVariable String teacherId,
                                       @PathVariable String courseId,
                                       @RequestBody Set<String> rights) {

        log.info("[CONTROLLER] Setting teacher rights");
        log.debug("[CONTROLLER] Setting teacher rights: {}, for teacher: {}, in course: {}",
                rights, teacherId, courseId);

        return ResponseEntity.ok(
                teacherRightsService.setRights(courseId, teacherId, rights)
        );
    }

    @GetMapping("/has")
    public Boolean hasRights(@PathVariable String teacherId,
                             @PathVariable String courseId,
                             @RequestParam Set<String> rights) {

        log.info("[CONTROLLER] Checking teacher rights");
        log.debug("[CONTROLLER] Checking teacher rights: {}, for teacher: {}, in course: {}",
                rights, teacherId, courseId);

        return teacherRightsService.hasRights(courseId, teacherId, rights);
    }
}
