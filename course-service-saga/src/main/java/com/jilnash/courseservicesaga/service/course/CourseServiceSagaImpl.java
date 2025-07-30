package com.jilnash.courseservicesaga.service.course;

import com.jilnash.courserightsservicedto.dto.CheckRightsDTO;
import com.jilnash.courserightsservicedto.dto.CreateOwnerDTO;
import com.jilnash.courseservicedto.dto.course.CourseCreateDTO;
import com.jilnash.courseservicedto.dto.course.CourseUpdateDescriptionDTO;
import com.jilnash.courseservicedto.dto.course.CourseUpdateDurationDTO;
import com.jilnash.courseservicedto.dto.course.CourseUpdateNameDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceSagaImpl implements CourseServiceSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void createCourse(CourseCreateDTO dto) {

        String transactionId = UUID.randomUUID().toString();
        String uuid = UUID.randomUUID().toString();
        dto.setId(uuid);
        dto.setTransactionId(transactionId);

        kafkaTemplate.send("check-course-rights-topic",
                new CreateOwnerDTO(transactionId, uuid, dto.getAuthorId()));
        kafkaTemplate.send("course-create-topic", dto);
//            kafkaTemplate.send("course-owner-created-topic", dto.getAuthorId());

    }

    @Override
    public void updateCourseName(String teacherId, String courseId, String name) {

        String transactionId = UUID.randomUUID().toString();
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of()));
        kafkaTemplate.send("course-update-name-topic",
                new CourseUpdateNameDTO(transactionId, courseId, name));
    }

    @Override
    public void updateCourseDescription(String teacherId, String courseId, String description) {

        String transactionId = UUID.randomUUID().toString();
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of()));
        kafkaTemplate.send("course-update-description-topic",
                new CourseUpdateDescriptionDTO(transactionId, courseId, description));
    }

    @Override
    public void updateCourseDuration(String teacherId, String courseId, String duration) {

        String transactionId = UUID.randomUUID().toString();
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of()));
        kafkaTemplate.send("course-update-duration-topic",
                new CourseUpdateDurationDTO(transactionId, courseId, duration));
    }

    @Override
    public void softDeleteCourse(String teacherId, String courseId) {

        String transactionId = UUID.randomUUID().toString();
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of()));
        kafkaTemplate.send("course-soft-delete-topic", courseId);
    }

    @Override
    public void hardDeleteCourse(String teacherId, String courseId) {

        String transactionId = UUID.randomUUID().toString();
        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of()));
        kafkaTemplate.send("course-hard-delete-topic", courseId);
    }
}
