package com.jilnash.courseservice.listener;

import com.jilnash.courseservice.service.course.CourseServiceRollback;
import com.jilnash.courseservicedto.dto.course.CourseUpdateDescriptionDTO;
import com.jilnash.courseservicedto.dto.course.CourseUpdateDurationDTO;
import com.jilnash.courseservicedto.dto.course.CourseUpdateNameDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CourseRollbackListener {

    private final CourseServiceRollback courseServiceRollback;

    public CourseRollbackListener(CourseServiceRollback courseServiceRollback) {
        this.courseServiceRollback = courseServiceRollback;
    }

    @KafkaListener(topics = "course-create-rollback-topic", groupId = "course-service-group")
    public void handleCreateCourseRollback(String courseId) {
        courseServiceRollback.createCourseRollback(courseId);
    }

    @KafkaListener(topics = "course-name-update-rollback-topic", groupId = "course-service-group")
    public void handleUpdateCourseNameRollback(CourseUpdateNameDTO dto) {
        courseServiceRollback.updateCourseNameRollback(dto.id(), dto.name());
    }

    @KafkaListener(topics = "course-description-update-rollback-topic", groupId = "course-service-group")
    public void handleUpdateCourseDescriptionRollback(CourseUpdateDescriptionDTO dto) {
        courseServiceRollback.updateCourseDescriptionRollback(dto.id(), dto.description());
    }

    @KafkaListener(topics = "course-duration-update-rollback-topic", groupId = "course-service-group")
    public void handleUpdateCourseDurationRollback(CourseUpdateDurationDTO dto) {
        courseServiceRollback.updateCourseDurationRollback(dto.id(), dto.duration());
    }

    @KafkaListener(topics = "course-soft-delete-rollback-topic", groupId = "course-service-group")
    public void handleDeleteCourseRollback(String courseId) {
        courseServiceRollback.softDeleteRollback(courseId);
    }
}
