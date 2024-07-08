package com.jilnash.courseservice.mapper;

import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.dto.task.TaskUpdateDTO;
import com.jilnash.courseservice.model.Module;
import com.jilnash.courseservice.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskCreateDTO taskDTO) {

        return Task.builder()
                .module(Module.builder().id(taskDTO.getModuleId()).build())
                .author(taskDTO.getAuthor())
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .videoLink(taskDTO.getVideoLink())
                .audioRequired(taskDTO.getAudioRequired())
                .videoRequired(taskDTO.getVideoRequired())
                .build();
    }

    public Task toEntity(TaskUpdateDTO taskDTO) {

        return Task.builder()
                .id(taskDTO.getId())
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .videoLink(taskDTO.getVideoLink())
                .audioRequired(taskDTO.getAudioRequired())
                .videoRequired(taskDTO.getVideoRequired())
                .build();
    }
}
