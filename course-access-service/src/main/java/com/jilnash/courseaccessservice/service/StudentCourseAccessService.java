package com.jilnash.courseaccessservice.service;

import com.jilnash.courseaccessservice.model.StudentCourseAccess;
import com.jilnash.courseaccessservice.repo.StudentCourseAccessRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentCourseAccessService {

    private final StudentCourseAccessRepo studentCourseAccessRepo;

    public Boolean getStudentHasAccess(String studentId, String courseId) {

        log.info("[SERVICE] Getting student has access to course");
        log.debug("[SERVICE] Getting studentId: {}, has access to courseId: {}", studentId, courseId);

        return studentCourseAccessRepo
                .existsByStudentIdAndCourseIdAndStartDateBeforeAndEndDateAfter(
                        studentId, courseId, new Date(System.currentTimeMillis())
                );
    }

    public StudentCourseAccess purchase(StudentCourseAccess studentCourseAccess) {

        log.info("[SERVICE] Purchasing course");
        log.debug("[SERVICE] Purchasing course for studentId: {}, courseId: {}",
                studentCourseAccess.getStudentId(), studentCourseAccess.getCourseId());

        // check if student already has access to this course
        if (studentCourseAccessRepo
                .existsByStudentIdAndCourseIdAndStartDateBeforeAndEndDateAfter(
                        studentCourseAccess.getStudentId(),
                        studentCourseAccess.getCourseId(),
                        new Date(System.currentTimeMillis())
                )
        ) {
            throw new RuntimeException("Student already has access to this course");
        }

        return studentCourseAccessRepo.save(studentCourseAccess);
    }
}
