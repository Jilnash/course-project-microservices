package com.jilnash.hwservicesaga.service;

import com.jilnash.hwservicedto.dto.HomeworkResponse;
import com.jilnash.hwservicesaga.dto.HomeworkCreateSagaDTO;
import com.jilnash.hwservicesaga.mapper.HomeworkMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Service
public class HomeworkSagaServiceImpl implements HomeworkSagaService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final HomeworkMapper homeworkMapper;

    public HomeworkSagaServiceImpl(KafkaTemplate<String, Object> kafkaTemplate, HomeworkMapper homeworkMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.homeworkMapper = homeworkMapper;
    }

    @Override
    public List<HomeworkResponse> getHomeworks(String taskId, String studentId, Boolean checked, Date createdAfter) {
        //todo: check user permissions
        return List.of();
    }

    @Override
    public HomeworkResponse getHomework(UUID id) {
        //todo: check user permissions
        return null;
    }

    @Override
    public void setHomeworkChecked(UUID id) {
        //todo: check user permissions
        kafkaTemplate.send("homework-checked-topic", id);
    }

    @Override
    public void createHomework(HomeworkCreateSagaDTO homework) {
        //todo: check user permissions
        kafkaTemplate.send("homework-create-topic", homeworkMapper.homeworkCreateDTO(homework));
    }

    @Override
    public void softDeleteHomework(UUID id) {
        //todo: check user permissions
        kafkaTemplate.send("homework-soft-delete-topic", id);
    }

    @Override
    public void hardDeleteHomework(UUID id) {
        //todo: check user permissions
        kafkaTemplate.send("homework-hard-delete-topic", id);
    }
}
