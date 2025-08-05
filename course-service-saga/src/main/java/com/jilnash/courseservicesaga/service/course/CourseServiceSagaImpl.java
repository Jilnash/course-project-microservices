package com.jilnash.courseservicesaga.service.course;

import com.jilnash.courserightsservicedto.dto.CheckRightsDTO;
import com.jilnash.courserightsservicedto.dto.CreateOwnerDTO;
import com.jilnash.courseservicedto.dto.course.CourseCreateDTO;
import com.jilnash.courseservicedto.dto.course.CourseUpdateDescriptionDTO;
import com.jilnash.courseservicedto.dto.course.CourseUpdateDurationDTO;
import com.jilnash.courseservicedto.dto.course.CourseUpdateNameDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseServiceSagaImpl implements CourseServiceSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final HttpServletRequest request;

    @Override
    public void createCourse(CourseCreateDTO dto) {

        String transactionId = request.getHeader("X-Transaction-Id");

        dto.setTransactionId(transactionId);

        kafkaTemplate.send("create-course-owner-topic",
                new CreateOwnerDTO(transactionId, dto.getId(), dto.getAuthorId()));
        kafkaTemplate.send("course-create-topic", dto);
    }

    @Override
    public void updateCourseName(String teacherId, String courseId, String name) {

        String transactionId = request.getHeader("X-Transaction-Id");

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("UPDATE")));
        kafkaTemplate.send("course-update-name-topic",
                new CourseUpdateNameDTO(transactionId, courseId, name));
    }

    @Override
    public void updateCourseDescription(String teacherId, String courseId, String description) {

        String transactionId = request.getHeader("X-Transaction-Id");

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("UPDATE")));
        kafkaTemplate.send("course-update-description-topic",
                new CourseUpdateDescriptionDTO(transactionId, courseId, description));
    }

    @Override
    public void updateCourseDuration(String teacherId, String courseId, String duration) {

        String transactionId = request.getHeader("X-Transaction-Id");

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("UPDATE")));
        kafkaTemplate.send("course-update-duration-topic",
                new CourseUpdateDurationDTO(transactionId, courseId, duration));
    }

    @Override
    public void softDeleteCourse(String teacherId, String courseId) {

        String transactionId = request.getHeader("X-Transaction-Id");

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("DELETE")));
        kafkaTemplate.send("course-soft-delete-topic", courseId);
    }

    @Override
    public void hardDeleteCourse(String teacherId, String courseId) {

        String transactionId = request.getHeader("X-Transaction-Id");

        kafkaTemplate.send("check-course-rights-topic",
                new CheckRightsDTO(transactionId, courseId, teacherId, Set.of("DELETE")));
        kafkaTemplate.send("course-hard-delete-topic", courseId);
    }
}
