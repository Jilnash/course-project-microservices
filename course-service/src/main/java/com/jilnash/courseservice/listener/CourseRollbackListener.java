package com.jilnash.courseservice.listener;

import com.jilnash.courseservice.service.course.CourseServiceRollback;
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
    public void handleUpdateCourseNameRollback(String courseId) {
        courseServiceRollback.updateCourseNameRollback(courseId);
    }

    @KafkaListener(topics = "course-description-update-rollback-topic", groupId = "course-service-group")
    public void handleUpdateCourseDescriptionRollback(String courseId) {
        courseServiceRollback.updateCourseDescriptionRollback(courseId);
    }

    @KafkaListener(topics = "course-duration-update-rollback-topic", groupId = "course-service-group")
    public void handleUpdateCourseDurationRollback(String courseId) {
        courseServiceRollback.updateCourseDurationRollback(courseId);
    }

    @KafkaListener(topics = "course-soft-delete-rollback-topic", groupId = "course-service-group")
    public void handleDeleteCourseRollback(String courseId) {
        courseServiceRollback.softDeleteRollback(courseId);
    }
}
