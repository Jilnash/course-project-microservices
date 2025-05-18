package com.jilnash.courseservice.converter.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jilnash.courseservice.dto.task.TaskLinkDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTaskLinkDTOConverter implements Converter<String, TaskLinkDTO> {

    private final ObjectMapper objectMapper;

    public StringToTaskLinkDTOConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Converts a JSON string representation into an instance of TaskLinkDTO.
     * Which is needed in task creation endpoint.
     *
     * @param source the JSON string to be converted
     * @return an instance of TaskLinkDTO parsed from the given string
     */
    @Override
    public TaskLinkDTO convert(String source) {
        return objectMapper.convertValue(source, TaskLinkDTO.class);
    }
}
