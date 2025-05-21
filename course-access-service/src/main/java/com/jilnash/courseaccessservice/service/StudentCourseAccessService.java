package com.jilnash.courseaccessservice.service;

import com.jilnash.courseaccessservice.model.StudentCourseAccess;
import com.jilnash.courseaccessservice.repo.StudentCourseAccessRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;

/**
 * Service class responsible for managing student access to courses.
 * Provides methods to check if a student has access to a course and to handle course purchases.
 * Relies on the {@link StudentCourseAccessRepo} for database interactions.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentCourseAccessService {

    private final StudentCourseAccessRepo studentCourseAccessRepo;

    /**
     * Checks if a student has access to a specific course based on the current date and their access period.
     *
     * @param studentId the unique identifier of the student
     * @param courseId  the unique identifier of the course
     * @return true if the student has access to the course, false otherwise
     */
    public Boolean getStudentHasAccess(String studentId, String courseId) {

        log.info("[SERVICE] Getting student has access to course");
        log.debug("[SERVICE] Getting studentId: {}, has access to courseId: {}", studentId, courseId);

        return studentCourseAccessRepo
                .existsByStudentIdAndCourseIdAndStartDateBeforeAndEndDateAfter(
                        studentId, courseId, new Date(System.currentTimeMillis())
                );
    }

    /**
     * Processes the purchase of a course for a student. Verifies if the student already has access
     * to the course before saving the new access. Throws an exception if the student already
     * has access to the course.
     *
     * @param studentCourseAccess an object representing the details of the course access
     *                             being purchased, including studentId, courseId, startDate, and endDate
     * @return the saved {@code StudentCourseAccess} object representing the purchased course access
     * @throws RuntimeException if the student already has access to the specified course
     */
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
