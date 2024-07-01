package com.jilnash.studentservice.mapper;

import com.jilnash.studentservice.dto.StudentDTO;
import com.jilnash.studentservice.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentDTO toDTO(Student student) {

        return new StudentDTO(
                student.getId(),
                student.getUserId(),
                student.getName(),
                student.getSurname(),
                student.getLink()
        );
    }

    public Student toEntity(StudentDTO studentDTO) {

        return Student.builder()
                .id(studentDTO.getId())
                .userId(studentDTO.getUserId())
                .name(studentDTO.getName())
                .surname(studentDTO.getSurname())
                .link(studentDTO.getLink())
                .build();
    }
}
