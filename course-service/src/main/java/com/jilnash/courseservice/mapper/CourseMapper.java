package com.jilnash.courseservice.mapper;

import com.jilnash.courseservice.dto.course.CourseCreateDTO;
import com.jilnash.courseservice.dto.course.CourseUpdateDTO;
import com.jilnash.courseservice.model.Course;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CourseMapper {

    public Course toNode(CourseCreateDTO courseCreateDTO) {
        return Course.builder()
                .name(courseCreateDTO.getName())
                .description(courseCreateDTO.getDescription())
                .duration(courseCreateDTO.getDuration())
                .hwPostingDayInterval(courseCreateDTO.getHwPostingDayInterval())
                .createdAt(new Date())
                .build();
    }

    public Course toNode(CourseUpdateDTO courseUpdateDTO) {
        return Course.builder()
                .id(courseUpdateDTO.getId())
                .name(courseUpdateDTO.getName())
                .description(courseUpdateDTO.getDescription())
                .duration(courseUpdateDTO.getDuration())
                .hwPostingDayInterval(courseUpdateDTO.getHwPostingDayInterval())
                .updatedAt(new Date())
                .build();
    }
}
