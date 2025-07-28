package com.jilnash.courseservicesaga.service.course;

import com.jilnash.courseservicedto.dto.course.CourseUpdateDescriptionDTO;
import com.jilnash.courseservicedto.dto.course.CourseUpdateDurationDTO;
import com.jilnash.courseservicedto.dto.course.CourseUpdateNameDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceRollbackSagaImpl implements CourseServiceRollbackSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CourseServiceRollbackSagaImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void createCourseRollback(String courseId) {
        kafkaTemplate.send("course-create-rollback-topic", courseId);
    }

    @Override
    public void updateCourseNameRollback(String courseId, String prevName) {
        kafkaTemplate.send("course-name-update-rollback-topic", new CourseUpdateNameDTO(courseId, prevName));
    }

    @Override
    public void updateCourseDescriptionRollback(String courseId, String prevDescription) {
        kafkaTemplate.send("course-description-update-rollback-topic", new CourseUpdateDescriptionDTO(courseId, prevDescription));
    }

    @Override
    public void updateCourseDurationRollback(String courseId, String prevDuration) {
        kafkaTemplate.send("course-duration-update-rollback-topic", new CourseUpdateDurationDTO(courseId, prevDuration));
    }

    @Override
    public void deleteCourseRollback(String courseId) {
        kafkaTemplate.send("course-soft-delete-rollback-topic", courseId);
    }
}
