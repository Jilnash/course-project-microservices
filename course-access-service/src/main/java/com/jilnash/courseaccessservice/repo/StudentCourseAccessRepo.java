package com.jilnash.courseaccessservice.repo;

import com.jilnash.courseaccessservice.model.StudentCourseAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface StudentCourseAccessRepo extends JpaRepository<StudentCourseAccess, Long> {

    @Query("""
            select (count(s) > 0) from student_course_access s
            where s.studentId = ?1 and s.courseId = ?2 and s.startDate < ?3 and s.endDate > ?3""")
    Boolean existsByStudentIdAndCourseIdAndStartDateBeforeAndEndDateAfter(String studentId, String courseId, Date currentDate);

    StudentCourseAccess findTopByStudentIdAndCourseIdOrderByCreatedAtDesc(String studentId, String courseId);
}