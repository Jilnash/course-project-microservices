package com.jilnash.courserightsservice.repo;

import com.jilnash.courserightsservice.model.TeacherRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRightsRepo extends JpaRepository<TeacherRights, Long> {

    Optional<TeacherRights> getTeacherRightsByTeacherIdAndCourseId(String teacherId, String courseId);
}
