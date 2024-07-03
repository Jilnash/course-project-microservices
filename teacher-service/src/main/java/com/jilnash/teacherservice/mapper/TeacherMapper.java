package com.jilnash.teacherservice.mapper;

import com.jilnash.teacherservice.dto.TeacherCreateDTO;
import com.jilnash.teacherservice.dto.TeacherUpdateDTO;
import com.jilnash.teacherservice.model.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {

    public Teacher toEntity(TeacherCreateDTO teacherDTO) {

        return Teacher.builder()
                .userId(teacherDTO.getUserId())
                .name(teacherDTO.getName())
                .surname(teacherDTO.getSurname())
                .description(teacherDTO.getDescription())
                .photo(teacherDTO.getPhoto())
                .mediaLink(teacherDTO.getMediaLink())
                .build();
    }

    public Teacher toEntity(TeacherUpdateDTO teacherDTO) {

        return Teacher.builder()
                .id(teacherDTO.getId())
                .userId(teacherDTO.getUserId())
                .name(teacherDTO.getName())
                .surname(teacherDTO.getSurname())
                .description(teacherDTO.getDescription())
                .photo(teacherDTO.getPhoto())
                .mediaLink(teacherDTO.getMediaLink())
                .build();
    }
}
