package com.jilnash.courseservice.converter.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jilnash.taskfilerequirementsservice.dto.TaskFileReqDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTaskFileReqDTOConverter implements Converter<String, TaskFileReqDTO> {

    private final ObjectMapper objectMapper;

    public StringToTaskFileReqDTOConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Converts a JSON string representation into an instance of TaskFileReqDTO.
     * Which is needed in create task endpoint.
     *
     * @param source the JSON string to be converted
     * @return an instance of TaskFileReqDTO parsed from the given string
     */
    @Override
    public TaskFileReqDTO convert(String source) {
        return objectMapper.convertValue(source, TaskFileReqDTO.class);
    }
}
