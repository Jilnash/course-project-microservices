package com.jilnash.teacherservice.service;

import com.jilnash.teacherservice.model.Teacher;
import com.jilnash.teacherservice.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TeacherServiceImplV1 implements TeacherService {

    @Autowired
    private TeacherRepo teacherRepo;

    @Override
    public List<Teacher> getTeachers() {
        return teacherRepo.findAll();
    }

    @Override
    public List<Teacher> getTeachers(String login) {

        //get user ids by login
        List<Long> userIds = List.of();

        return teacherRepo.findByUserIds(userIds);
    }

    @Override
    public Teacher getTeacher(Long id) {
        return teacherRepo
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Teacher not found with id: " + id));
    }

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepo.save(teacher);
    }
}
