package com.jilnash.courserightsservice.service;

import com.jilnash.courserightsservice.repo.TeacherRightsRepo;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TeacherRightsServiceRollback {

    private final TeacherRightsRepo teacherRightsRepo;

    public TeacherRightsServiceRollback(TeacherRightsRepo teacherRightsRepo) {
        this.teacherRightsRepo = teacherRightsRepo;
    }

    public void createCourseOwnerRollback(String courseId, String teacherId) {

        teacherRightsRepo.deleteAllByCourseIdAndTeacherId(courseId, teacherId);
    }

    public void setRightsRollback(String courseId, String teacherId, Set<String> rights) {
        try {
            teacherRightsRepo.deleteAllByTeacherIdAndCourseIdAndDeletedAtIsNotNull(teacherId, courseId);
            teacherRightsRepo.updateDeletedAtByCourseIdAndTeacherId(courseId, teacherId, null);
        } catch (Exception e) {
            // Log the exception if necessary
        }
    }
}
