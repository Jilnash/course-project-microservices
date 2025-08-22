package com.jilnash.homeworkservice.listener;

import com.jilnash.homeworkservice.mapper.HomeworkMapper;
import com.jilnash.homeworkservice.service.HomeworkService;
import com.jilnash.hwservicedto.dto.HomeworkCheckDTO;
import com.jilnash.hwservicedto.dto.HomeworkCreateDTO;
import com.jilnash.hwservicedto.dto.HomeworkDeleteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HomeworkListener {

    private final HomeworkService homeworkService;
    private final HomeworkMapper homeworkMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "homework-checked-topic", groupId = "homework-service")
    public void handleHomeworkChecked(HomeworkCheckDTO dto) {
        try {
            homeworkService.setHomeworkChecked(dto.homeworkId());
        } catch (Exception e) {
            kafkaTemplate.send("rollback-topic", dto.transactionId());
        }
    }

    @KafkaListener(topics = "homework-create-topic", groupId = "homework-service")
    public void handleHomeworkCreate(HomeworkCreateDTO dto) {
        try {
            homeworkService.createHomework(homeworkMapper.toEntity(dto));
        } catch (Exception e) {
            kafkaTemplate.send("rollback-topic", dto.transactionId());
        }
    }

    @KafkaListener(topics = "homework-soft-delete-topic", groupId = "homework-service")
    public void handleHomeworkSoftDelete(HomeworkDeleteDTO dto) {
        try {
            homeworkService.softDeleteHomework(dto.homeworkId());
        } catch (Exception e) {
            kafkaTemplate.send("rollback-topic", dto.transactionId());
        }
    }

    @KafkaListener(topics = "homework-hard-delete-topic", groupId = "homework-service")
    public void handleHomeworkHardDelete(HomeworkDeleteDTO dto) {
        try {
            homeworkService.hardDeleteHomework(dto.homeworkId());
        } catch (Exception e) {
            kafkaTemplate.send("rollback-topic", dto.transactionId());
        }
    }
}
