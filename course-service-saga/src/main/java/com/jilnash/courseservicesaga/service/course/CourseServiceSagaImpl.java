package com.jilnash.courseservicesaga.service.course;

import com.jilnash.courseservicedto.dto.course.CourseCreateDTO;
import com.jilnash.courseservicedto.dto.course.CourseUpdateDescriptionDTO;
import com.jilnash.courseservicedto.dto.course.CourseUpdateDurationDTO;
import com.jilnash.courseservicedto.dto.course.CourseUpdateNameDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceSagaImpl implements CourseServiceSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void createCourse(CourseCreateDTO courseCreateDTO) {
        String uuid = UUID.randomUUID().toString();
        courseCreateDTO.setId(uuid);
        try {
            kafkaTemplate.send("course-create-topic", courseCreateDTO);
//            kafkaTemplate.send("course-owner-created-topic", courseCreateDTO.getAuthorId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCourseName(String courseId, String name) {
        //todo: send the teacher rights validation event to Kafka
        kafkaTemplate.send("course-update-name-topic", new CourseUpdateNameDTO(courseId, name));
    }

    @Override
    public void updateCourseDescription(String courseId, String description) {
        //todo: send the teacher rights validation event to Kafka
        kafkaTemplate.send("course-update-description-topic", new CourseUpdateDescriptionDTO(courseId, description));
    }

    @Override
    public void updateCourseDuration(String courseId, String duration) {
        kafkaTemplate.send("course-update-duration-topic", new CourseUpdateDurationDTO(courseId, duration));
    }

    @Override
    public void softDeleteCourse(String courseId) {
        //todo: send the teacher rights validation event to Kafka
        kafkaTemplate.send("course-soft-delete-topic", courseId);
    }

    @Override
    public void hardDeleteCourse(String courseId) {
        //todo: send the teacher rights validation event to Kafka
        kafkaTemplate.send("course-hard-delete-topic", courseId);
    }
}
