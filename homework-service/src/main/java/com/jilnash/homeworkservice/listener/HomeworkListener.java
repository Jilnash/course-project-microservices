package com.jilnash.homeworkservice.listener;

import com.jilnash.homeworkservice.mapper.HomeworkMapper;
import com.jilnash.homeworkservice.service.HomeworkService;
import com.jilnash.hwservicedto.dto.HomeworkCreateDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HomeworkListener {

    private final HomeworkService homeworkService;

    private final HomeworkMapper homeworkMapper;

    public HomeworkListener(HomeworkService homeworkService, HomeworkMapper homeworkMapper) {
        this.homeworkService = homeworkService;
        this.homeworkMapper = homeworkMapper;
    }

    @KafkaListener(topics = "homework-checked-topic", groupId = "homework-service")
    public void handleHomeworkChecked(UUID homeworkId) {
        homeworkService.setHomeworkChecked(homeworkId);
    }

    @KafkaListener(topics = "homework-create-topic", groupId = "homework-service")
    public void handleHomeworkCreate(HomeworkCreateDTO homeworkCreateDTO) {
        homeworkService.createHomework(homeworkMapper.toEntity(homeworkCreateDTO));
    }

    @KafkaListener(topics = "homework-soft-delete-topic", groupId = "homework-service")
    public void handleHomeworkSoftDelete(UUID homeworkId) {
        homeworkService.softDeleteHomework(homeworkId);
    }

    @KafkaListener(topics = "homework-hard-delete-topic", groupId = "homework-service")
    public void handleHomeworkHardDelete(UUID homeworkId) {
        homeworkService.hardDeleteHomework(homeworkId);
    }
}
