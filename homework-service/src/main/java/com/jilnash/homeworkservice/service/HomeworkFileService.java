package com.jilnash.homeworkservice.service;

import com.jilnash.homeworkservice.model.Homework;
import com.jilnash.homeworkservice.model.HomeworkFile;
import com.jilnash.homeworkservice.model.HomeworkFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeworkFileService {

    private final HomeworkFileRepository homeworkFileRepository;

    @Async
    void createdHomeworkFiles(Homework homework) {

        homeworkFileRepository.saveAll(
                homework.getFiles().stream().map(file ->
                        HomeworkFile.builder()
                                .fileName(file.getOriginalFilename())
                                .homeworkId(homework)
                                .build()
                ).toList()
        );
    }
}
