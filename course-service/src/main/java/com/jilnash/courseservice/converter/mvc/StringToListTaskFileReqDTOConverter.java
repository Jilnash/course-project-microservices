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
