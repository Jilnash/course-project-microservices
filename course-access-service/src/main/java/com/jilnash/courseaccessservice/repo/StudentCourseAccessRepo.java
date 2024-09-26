package com.jilnash.courseaccessservice.repo;

import com.jilnash.courseaccessservice.model.StudentCourseAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface StudentCourseAccessRepo extends JpaRepository<StudentCourseAccess, Long> {

    Boolean existsByStudentIdAndCourseIdAndStartDateBeforeAndEndDateAfter(Long studentId, String courseId, Date currentDate);
}
