package com.jilnash.courserightsservice.repo;

import com.jilnash.courserightsservice.model.TeacherRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TeacherRightsRepo extends JpaRepository<TeacherRights, Long> {

    @Query("select tr.right.name from TeacherRights tr where tr.teacherId = ?1 and tr.courseId = ?2")
    Set<String> getTeacherRightsByTeacherIdAndCourseId(String teacherId, String courseId);

    void deleteAllByCourseIdAndTeacherId(String courseId, String teacherId);
}
