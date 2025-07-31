package com.jilnash.hwservicesaga.service;

import com.jilnash.courseaccessservicedto.dto.CheckAccessDTO;
import com.jilnash.courserightsservicedto.dto.CheckRightsDTO;
import com.jilnash.hwservicedto.dto.HomeworkResponse;
import com.jilnash.hwservicesaga.dto.HomeworkCreateSagaDTO;
import com.jilnash.hwservicesaga.mapper.HomeworkMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Set;
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
        return null;
    }

    @Override
    public void setHomeworkChecked(String courseId, String teacherId, UUID id) {

        String transactionId = UUID.randomUUID().toString();
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of()));

        kafkaTemplate.send("homework-checked-topic", id);
    }

    @Override
    public void createHomework(HomeworkCreateSagaDTO homework) {

        String transactionId = UUID.randomUUID().toString();
        kafkaTemplate.send("check-course-access-topic",
                new CheckAccessDTO(transactionId, homework.getCourseId(), homework.getStudentId()));
        //todo: upload files to storage
        kafkaTemplate.send("homework-create-topic", homeworkMapper.homeworkCreateDTO(homework));
    }

    @Override
    public void softDeleteHomework(String courseId, String teacherId, UUID id) {

        String transactionId = UUID.randomUUID().toString();
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of()));

        kafkaTemplate.send("homework-soft-delete-topic", id);
    }

    @Override
    public void hardDeleteHomework(String courseId, String teacherId, UUID id) {

        String transactionId = UUID.randomUUID().toString();
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of()));

        kafkaTemplate.send("homework-hard-delete-topic", id);
    }
}
