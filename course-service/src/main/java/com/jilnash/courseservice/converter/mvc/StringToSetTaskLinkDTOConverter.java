package com.jilnash.courseservice.converter.mvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jilnash.courseservice.dto.task.TaskLinkDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class StringToSetTaskLinkDTOConverter implements Converter<String, Set<TaskLinkDTO>> {

    private final ObjectMapper objectMapper;

    public StringToSetTaskLinkDTOConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Set<TaskLinkDTO> convert(String source) {
        try {
            return objectMapper.readValue(source, new TypeReference<Set<TaskLinkDTO>>() {
            });
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert String to Set<TaskLinkDTO>", e);
        }
    }
}
