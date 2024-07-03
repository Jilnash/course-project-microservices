package com.jilnash.studentservice.mapper;

import com.jilnash.studentservice.dto.StudentCreateDTO;
import com.jilnash.studentservice.dto.StudentUpdateDTO;
import com.jilnash.studentservice.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public Student toEntity(StudentCreateDTO studentDTO) {

        return Student.builder()
                .userId(studentDTO.getUserId())
                .name(studentDTO.getName())
                .surname(studentDTO.getSurname())
                .link(studentDTO.getLink())
                .build();
    }

    public Student toEntity(StudentUpdateDTO studentDTO) {

        return Student.builder()
                .id(studentDTO.getId())
                .userId(studentDTO.getUserId())
                .name(studentDTO.getName())
                .surname(studentDTO.getSurname())
                .link(studentDTO.getLink())
                .build();
    }
}
