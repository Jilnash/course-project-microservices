package com.jilnash.teacherservice.service;

import com.jilnash.teacherservice.model.Teacher;

import java.util.List;

public interface TeacherService {

    List<Teacher> getTeachers();

    List<Teacher> getTeachers(String login);

    Teacher getTeacher(Long id);

    Teacher saveTeacher(Teacher teacher);
}
