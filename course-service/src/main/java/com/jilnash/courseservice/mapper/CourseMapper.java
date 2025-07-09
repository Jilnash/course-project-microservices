package com.jilnash.courseservice.mapper;

import com.jilnash.courseservice.dto.course.CourseCreateDTO;
import com.jilnash.courseservice.dto.course.CourseUpdateDTO;
import com.jilnash.courseservice.model.Course;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Mapper class responsible for the conversion between Course entities and data transfer objects (DTOs).
 * This class provides utility methods to map between CourseCreateDTO, CourseUpdateDTO, and the Course entity.
 */
@Component
public class CourseMapper {

    /**
     * Converts a CourseCreateDTO object into a Course entity.
     *
     * @param courseCreateDTO the CourseCreateDTO object containing the details for the course to be converted
     * @return a Course entity constructed based on the provided CourseCreateDTO
     */
    public Course toNode(CourseCreateDTO courseCreateDTO) {
        return Course.builder()
                .id(courseCreateDTO.getId())
                .createdBy(courseCreateDTO.getAuthorId())
                .name(courseCreateDTO.getName())
                .description(courseCreateDTO.getDescription())
                .duration(courseCreateDTO.getDuration())
                .createdAt(new Date())
                .build();
    }

    /**
     * Converts a CourseUpdateDTO object to a Course entity.
     *
     * @param courseUpdateDTO the CourseUpdateDTO object containing the updated details for the course
     * @return a Course entity constructed based on the provided CourseUpdateDTO
     */
    public Course toNode(CourseUpdateDTO courseUpdateDTO) {
        return Course.builder()
                .id(courseUpdateDTO.getId())
                .name(courseUpdateDTO.getName())
                .description(courseUpdateDTO.getDescription())
                .duration(courseUpdateDTO.getDuration())
                .updatedAt(new Date())
                .build();
    }
}
