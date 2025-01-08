package com.jilnash.homeworkservice.mapper;

import com.jilnash.homeworkservice.dto.HomeworkCreateDTO;
import com.jilnash.homeworkservice.model.Homework;
import org.springframework.stereotype.Component;

@Component
public class HomeworkMapper {

    public Homework toEntity(HomeworkCreateDTO homeworkDTO) {

        return Homework.builder()
                .studentId(homeworkDTO.getStudentId())
                .taskId(homeworkDTO.getTaskId())
                .checked(homeworkDTO.getChecked())
                .files(homeworkDTO.getFiles())
                .build();
    }
}
