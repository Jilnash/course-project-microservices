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

    public Boolean createCourseOwnerRollback(String courseId, String teacherId) {

        teacherRightsRepo.deleteAllByCourseIdAndTeacherId(courseId, teacherId);

        return false;
    }

    public Boolean setRightsRollback(String courseId, String teacherId, Set<String> rights) {
        try {
            teacherRightsRepo.deleteAllByTeacherIdAndCourseIdAndDeletedAtIsNotNull(teacherId, courseId);
            teacherRightsRepo.updateDeletedAtByCourseIdAndTeacherId(courseId, teacherId, null);
            return true;
        } catch (Exception e) {
            // Log the exception if necessary
            return false;
        }
    }
}
