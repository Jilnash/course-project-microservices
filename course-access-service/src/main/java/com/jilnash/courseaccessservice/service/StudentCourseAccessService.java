package com.jilnash.courseaccessservice.service;

import com.jilnash.courseaccessservice.model.StudentCourseAccess;
import com.jilnash.courseaccessservice.repo.StudentCourseAccessRepo;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class StudentCourseAccessService {

    private final StudentCourseAccessRepo studentCourseAccessRepo;

    public StudentCourseAccessService(StudentCourseAccessRepo studentCourseAccessRepo) {
        this.studentCourseAccessRepo = studentCourseAccessRepo;
    }

    public Boolean getStudentHasAccess(Long studentId, String courseId) {
        return studentCourseAccessRepo
                .existsByStudentIdAndCourseIdAndStartDateBeforeAndEndDateAfter(
                        studentId, courseId, new Date(System.currentTimeMillis())
                );
    }

    public StudentCourseAccess create(StudentCourseAccess studentCourseAccess) {
        return studentCourseAccessRepo.save(studentCourseAccess);
    }
}
