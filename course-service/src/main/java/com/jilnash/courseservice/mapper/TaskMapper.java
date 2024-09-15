package com.jilnash.courseservice.mapper;

import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toNode(TaskCreateDTO taskCreateDTO) {
        return Task.builder()
                .title(taskCreateDTO.getTitle())
                .description(taskCreateDTO.getDescription())
                .videoLink(taskCreateDTO.getVideoLink())
                .audioRequired(taskCreateDTO.getAudioRequired())
                .videoRequired(taskCreateDTO.getVideoRequired())
                .module(taskCreateDTO.getModule())
                .build();
    }
}
