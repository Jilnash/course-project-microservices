package com.jilnash.courserightsservice.service;

import com.jilnash.courserightsservice.model.TeacherRights;
import com.jilnash.courserightsservice.repo.TeacherRightsRepo;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class TeacherRightsService {

    private final TeacherRightsRepo teacherRightsRepo;

    public TeacherRightsService(TeacherRightsRepo teacherRightsRepo) {
        this.teacherRightsRepo = teacherRightsRepo;
    }

    public List<String> getRights(String courseId, String teacherId) {
        return teacherRightsRepo.getTeacherRightsByTeacherIdAndCourseId(teacherId, courseId)
                .map(teacherRights -> {
                    List<String> rights = new LinkedList<>();

                    if (teacherRights.getEditCourse()) rights.add("edit");
                    if (teacherRights.getAddTask()) rights.add("add");
                    if (teacherRights.getDeleteCourse()) rights.add("delete");
                    if (teacherRights.getManageTeachers()) rights.add("manageTeachers");


                    return rights;
                })
                .orElseThrow(() -> new RuntimeException("Teacher rights not found"));
    }


    public Boolean hasRights(String courseId, String teacherId, List<String> rights) {

        TeacherRights teacherRights = teacherRightsRepo.getTeacherRightsByTeacherIdAndCourseId(teacherId, courseId)
                .orElseThrow(() -> new RuntimeException("Teacher rights not found"));

        for (String right : rights) {
            switch (right) {
                case "edit":
                    if (!teacherRights.getEditCourse()) return false;
                    break;
                case "delete":
                    if (!teacherRights.getDeleteCourse()) return false;
                    break;
                case "add":
                    if (!teacherRights.getAddTask()) return false;
                    break;
                case "manageTeachers":
                    if (!teacherRights.getManageTeachers()) return false;
                    break;
            }
        }
        return true;
    }

    public TeacherRights create(String courseId, String teacherId, List<String> rights) {
        return teacherRightsRepo.save(new TeacherRights(courseId, teacherId, rights));
    }

    public TeacherRights update(String courseId, String teacherId, List<String> rights) {
        TeacherRights teacherRights = teacherRightsRepo.getTeacherRightsByTeacherIdAndCourseId(teacherId, courseId)
                .orElseThrow(() -> new RuntimeException("Teacher rights not found"));

        rights.forEach(right -> {
            switch (right) {
                case "edit":
                    teacherRights.setEditCourse(true);
                    break;
                case "delete":
                    teacherRights.setDeleteCourse(true);
                    break;
                case "add":
                    teacherRights.setAddTask(true);
                    break;
                case "manageTeachers":
                    teacherRights.setManageTeachers(true);
                    break;
            }
        });

        return teacherRightsRepo.save(teacherRights);
    }
}
