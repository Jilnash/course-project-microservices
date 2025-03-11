package com.jilnash.courseaccessservice.controller.v1;

import com.jilnash.courseaccessservice.model.StudentCourseAccess;
import com.jilnash.courseaccessservice.service.StudentCourseAccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@Slf4j
@RestController
@RequestMapping("api/v1/course-access")
@RequiredArgsConstructor
public class StudentCourseAccessServiceController {

    private final StudentCourseAccessService studentCourseAccessService;

    @GetMapping
    public Boolean getStudentHasAccess(@RequestParam String studentId, @RequestParam String courseId) {

        log.info("[CONTROLLER] Getting student has access to course");
        log.debug("[CONTROLLER] Getting studentId: {}, has access to courseId: {}", studentId, courseId);

        return studentCourseAccessService.getStudentHasAccess(studentId, courseId);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestParam String studentId, @RequestParam String courseId) {

        log.info("[CONTROLLER] Purchasing course");
        log.debug("[CONTROLLER] Purchasing course for studentId: {}, courseId: {}", studentId, courseId);

        return ResponseEntity.ok(
                studentCourseAccessService.purchase(
                        StudentCourseAccess.builder()
                                .studentId(studentId)
                                .courseId(courseId)
                                .startDate(new Date(System.currentTimeMillis()))
                                .endDate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))
                                .build()
                )
        );
    }
}
