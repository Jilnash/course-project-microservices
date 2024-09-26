package com.jilnash.courseaccessservice.controller.v1;

import com.jilnash.courseaccessservice.model.StudentCourseAccess;
import com.jilnash.courseaccessservice.service.StudentCourseAccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequestMapping("api/v1/course-access")
public class StudentCourseAccessServiceController {

    private final StudentCourseAccessService studentCourseAccessService;

    public StudentCourseAccessServiceController(StudentCourseAccessService studentCourseAccessService) {
        this.studentCourseAccessService = studentCourseAccessService;
    }

    @GetMapping
    public Boolean getStudentHasAccess(Long studentId, String courseId) {
        return studentCourseAccessService.getStudentHasAccess(studentId, courseId);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestParam Long studentId, @RequestParam String courseId) {
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
