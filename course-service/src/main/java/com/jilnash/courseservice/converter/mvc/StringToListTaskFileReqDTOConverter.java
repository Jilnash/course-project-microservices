package com.jilnash.courseservice.converter.mvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jilnash.taskfilerequirementsservice.dto.TaskFileReqDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StringToListTaskFileReqDTOConverter implements Converter<String, List<TaskFileReqDTO>> {

    private final ObjectMapper objectMapper;

    public StringToListTaskFileReqDTOConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Converts a JSON string representation of a list of TaskFileReqDTO objects
     * into a List<TaskFileReqDTO> instance using ObjectMapper. It is needed in task creation endpoint.
     *
     * @param source the JSON string to be converted
     * @return a List of TaskFileReqDTO objects parsed from the given string
     * @throws IllegalArgumentException if the conversion process encounters an error
     */
    @Override
    public List<TaskFileReqDTO> convert(String source) {
        try {
            return objectMapper.readValue(source, new TypeReference<List<TaskFileReqDTO>>() {
            });
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert String to List<TaskFileReqDTO>", e);
        }
    }
}
