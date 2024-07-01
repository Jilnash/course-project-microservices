package com.jilnash.studentservice.service;

import com.jilnash.studentservice.model.Student;

import java.util.List;

public interface StudentService {

    List<Student> getStudents();

    List<Student> getStudents(String login);

    Student getStudent(Long id);

    Student saveStudent(Student student);
}
