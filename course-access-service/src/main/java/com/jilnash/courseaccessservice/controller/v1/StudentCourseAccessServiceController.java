package com.jilnash.courseaccessservice.controller.v1;

import com.jilnash.courseaccessservice.model.StudentCourseAccess;
import com.jilnash.courseaccessservice.service.StudentCourseAccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

/**
 * Controller class responsible for managing student course access-related API endpoints.
 * Provides RESTful endpoints for checking student course access and handling course purchases.
 * Delegates business logic to the {@code StudentCourseAccessService}.
 */
@Slf4j
@RestController
@RequestMapping("api/v1/course-access")
@RequiredArgsConstructor
public class StudentCourseAccessServiceController {

    private final StudentCourseAccessService studentCourseAccessService;

    /**
     * Checks if a student has access to a specific course right now.
     *
     * @param studentId the unique identifier of the student
     * @param courseId  the unique identifier of the course
     * @return true if the student has access to the course, false otherwise
     */
    @GetMapping
    public Boolean getStudentHasAccess(@RequestParam String studentId, @RequestParam String courseId) {

        log.info("[CONTROLLER] Getting student has access to course");
        log.debug("[CONTROLLER] Getting studentId: {}, has access to courseId: {}", studentId, courseId);

        return studentCourseAccessService.getStudentHasAccess(studentId, courseId);
    }

    /**
     * Handles the creation of a new course purchase for a student. This method processes the
     * purchase request by saving the student's course access details, including start and end dates.
     *
     * @param studentId the unique identifier of the student purchasing the course
     * @param courseId  the unique identifier of the course being purchased
     * @return a {@code ResponseEntity} containing the result of the purchase operation
     */
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
