package com.jilnash.courseservice.listener;

import com.jilnash.courseservice.dto.course.CourseCreateDTO;
import com.jilnash.courseservice.dto.course.CourseUpdateDescriptionDTO;
import com.jilnash.courseservice.dto.course.CourseUpdateDurationDTO;
import com.jilnash.courseservice.dto.course.CourseUpdateNameDTO;
import com.jilnash.courseservice.service.course.CourseService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CourseListener {

    private final CourseService courseService;

    public CourseListener(CourseService courseService) {
        this.courseService = courseService;
    }

    @KafkaListener(topics = "course-create-topic", groupId = "course-service-group")
    public void createCourseListener(CourseCreateDTO courseCreateDTO) {
        try {
            courseService.createCourse(courseCreateDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error creating course with ID: " + courseCreateDTO.getId(), e);
        }
    }

    @KafkaListener(topics = "course-update-name-topic", groupId = "course-service-group")
    public void updateCourseNameListener(CourseUpdateNameDTO dto) {
        try {
            courseService.updateCourseName(dto.id(), dto.name());
        } catch (Exception e) {
            throw new RuntimeException("Error updating course name for ID: " + dto.id(), e);
        }
    }

    @KafkaListener(topics = "course-update-description-topic", groupId = "course-service-group")
    public void updateCourseDescriptionListener(CourseUpdateDescriptionDTO dto) {
        try {
            courseService.updateCourseDescription(dto.id(), dto.description());
        } catch (Exception e) {
            throw new RuntimeException("Error updating course description for ID: " + dto.id(), e);
        }
    }

    @KafkaListener(topics = "course-update-duration-topic", groupId = "course-service-group")
    public void updateCourseDurationListener(CourseUpdateDurationDTO dto) {
        try {
            courseService.updateCourseDuration(dto.id(), dto.duration());
        } catch (Exception e) {
            throw new RuntimeException("Error updating course duration for ID: " + dto.id(), e);
        }
    }

    @KafkaListener(topics = "course-soft-delete-topic", groupId = "course-service-group")
    public void softDeleteCourseListener(String courseId) {
        try {
            courseService.softDelete(courseId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting course with ID: " + courseId, e);
        }
    }

    @KafkaListener(topics = "course-hard-delete-topic", groupId = "course-service-group")
    public void hardDeleteCourseListener(String courseId) {
        try {
            courseService.hardDelete(courseId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting course with ID: " + courseId, e);
        }
    }
}