package com.jilnash.studentservice.service;

import com.jilnash.studentservice.model.Student;
import com.jilnash.studentservice.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImplV1 implements StudentService {

    @Autowired
    private StudentRepo studentRepo;

    @Override
    public List<Student> getStudents() {
        return studentRepo.findAll();
    }

    @Override
    public List<Student> getStudents(String login) {

        //send request to userService to get user ids by login
        List<Long> userIds = List.of();

        return studentRepo.findAllByUserIds(userIds);
    }

    @Override
    public Student getStudent(Long id) {
        return studentRepo.findById(id).orElseThrow();
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepo.save(student);
    }
}
