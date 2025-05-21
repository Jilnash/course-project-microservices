package com.jilnash.courserightsservice.controller.v1;

import com.jilnash.courserightsservice.service.TeacherRightsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Controller that manages teacher rights for courses.
 * Provides endpoints to fetch, set, and check teacher rights within a specific course context.
 */
@Slf4j
@RestController
@RequestMapping("api/v1/courses/{courseId}/teachers/{teacherId}/rights")
@RequiredArgsConstructor
public class RightsController {

    private final TeacherRightsService teacherRightsService;

    /**
     * Fetches the rights associated with a particular teacher for a specific course.
     *
     * @param teacherId the unique identifier of the teacher whose rights are being retrieved
     * @param courseId  the unique identifier of the course for which the teacher's rights are being fetched
     * @return a set of rights associated with the teacher for the given course
     */
    @GetMapping
    public Set<String> getRights(@PathVariable String teacherId, @PathVariable String courseId) {

        log.info("[CONTROLLER] Fetching teacher rights");
        log.debug("[CONTROLLER] Fetching teacher rights for teacher: {}, in course: {}", teacherId, courseId);

        return teacherRightsService.getRights(courseId, teacherId);
    }

    /**
     * Sets the rights for a specific teacher within a course.
     *
     * @param teacherId the unique identifier of the teacher for whom rights are being set
     * @param courseId  the unique identifier of the course in which the rights are being set
     * @param rights    a set of rights to assign to the teacher for the specified course
     * @return a {@code ResponseEntity} containing the result of the operation
     */
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

    /**
     * Checks if the specified teacher has the required rights for a particular course.
     *
     * @param teacherId the unique identifier of the teacher whose rights are being checked
     * @param courseId the unique identifier of the course for which the rights are being checked
     * @param rights a set of rights to verify against the teacher's permissions
     * @return true if the teacher has all the specified rights for the course, false otherwise
     */
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
