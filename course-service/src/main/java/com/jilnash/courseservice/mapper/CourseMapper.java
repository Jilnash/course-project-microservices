package com.jilnash.courseservice.mapper;

import com.jilnash.courseservice.dto.course.CourseCreateDTO;
import com.jilnash.courseservice.dto.course.CourseUpdateDTO;
import com.jilnash.courseservice.model.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public Course toEntity(CourseCreateDTO courseCreateDTO) {
        return Course.builder()
                .author(courseCreateDTO.getAuthorId())
                .name(courseCreateDTO.getName())
                .description(courseCreateDTO.getDescription())
                .duration(courseCreateDTO.getDuration())
                .hwPostingDayInterval(courseCreateDTO.getHwPostingDayInterval())
                .build();
    }

    public Course toEntity(CourseUpdateDTO courseUpdateDTO) {
        return Course.builder()
                .id(courseUpdateDTO.getId())
                .author(courseUpdateDTO.getAuthorId())
                .name(courseUpdateDTO.getName())
                .description(courseUpdateDTO.getDescription())
                .duration(courseUpdateDTO.getDuration())
                .hwPostingDayInterval(courseUpdateDTO.getHwPostingDayInterval())
                .build();
    }
}
