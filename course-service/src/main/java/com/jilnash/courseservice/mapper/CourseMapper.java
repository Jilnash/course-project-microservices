package com.jilnash.courseservice.mapper;

import com.jilnash.courseservice.model.Course;
import com.jilnash.courseservicedto.dto.course.CourseCreateDTO;
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
}
