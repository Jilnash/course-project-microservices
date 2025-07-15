package com.jilnash.courseaccessservice.service;

import com.jilnash.courseaccessservice.repo.StudentCourseAccessRepo;
import org.springframework.stereotype.Component;

@Component
public class StudentCourseAccessServieRollbackImpl implements StudentCourseAccessServiceRollback {

    private final StudentCourseAccessRepo studentCourseAccessRepo;

    public StudentCourseAccessServieRollbackImpl(StudentCourseAccessRepo studentCourseAccessRepo) {
        this.studentCourseAccessRepo = studentCourseAccessRepo;
    }

    /**
     * Rollback method for purchasing a course access.
     * This method is used to revert the purchase operation in case of an error.
     *
     * @param studentId the unique identifier of the student
     * @param courseId  the unique identifier of the course
     * @return the rolled back {@code StudentCourseAccess} object
     */
    public Boolean purchaseRollback(String studentId, String courseId) {
        // Logic to rollback the purchase operation
        // This could involve deleting the record or marking it as inactive
        // For now, we will just delete the record as an example

        studentCourseAccessRepo.delete(
                studentCourseAccessRepo.findTopByStudentIdAndCourseIdOrderByCreatedAtDesc(studentId, courseId)
        );

        return true; // Return the rolled back object
    }
}
