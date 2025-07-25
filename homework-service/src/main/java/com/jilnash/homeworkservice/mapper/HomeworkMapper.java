package com.jilnash.homeworkservice.mapper;

import com.jilnash.homeworkservice.dto.HomeworkResponseDTO;
import com.jilnash.homeworkservice.model.Homework;
import com.jilnash.homeworkservice.model.HomeworkFile;
import com.jilnash.hwservicedto.dto.HomeworkCreateDTO;
import org.springframework.stereotype.Component;

@Component
public class HomeworkMapper {

    public Homework toEntity(HomeworkCreateDTO homeworkDTO) {

        return Homework.builder()
                .studentId(homeworkDTO.studentId())
                .taskId(homeworkDTO.taskId())
                .checked(false)
                .hwFiles(
                        homeworkDTO.fileNames().stream().map(fileName -> HomeworkFile.builder()
                                .fileName(fileName)
                                .build()).toList()
                )
                .build();
    }

    public HomeworkResponseDTO toResponseDTO(Homework homework) {

        return HomeworkResponseDTO.builder()
                .id(homework.getId())
                .studentId(homework.getStudentId())
                .taskId(homework.getTaskId())
                .checked(homework.getChecked())
                .attempt(homework.getAttempt())
                .fileNames(homework.getHwFiles().stream().map(HomeworkFile::getFileName).toList())
                .build();
    }
}
