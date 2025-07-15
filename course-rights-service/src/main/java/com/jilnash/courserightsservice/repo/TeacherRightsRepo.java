package com.jilnash.courserightsservice.repo;

import com.jilnash.courserightsservice.model.TeacherRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

@Repository
public interface TeacherRightsRepo extends JpaRepository<TeacherRights, Long> {

    @Query("select tr.right.name from TeacherRights tr where tr.teacherId = ?1 and tr.courseId = ?2")
    Set<String> getTeacherRightsByTeacherIdAndCourseId(String teacherId, String courseId);

    void deleteAllByCourseIdAndTeacherId(String courseId, String teacherId);

    void deleteAllByTeacherIdAndCourseIdAndDeletedAtIsNotNull(String teacherId, String courseId);

    @Modifying
    @Query("update TeacherRights tr set tr.deletedAt = ?3 where tr.courseId = ?1 and tr.teacherId = ?2")
    void updateDeletedAtByCourseIdAndTeacherId(String courseId, String teacherId, Date deletedAt);
}
