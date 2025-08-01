package com.jilnash.courseservicesaga.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jilnash.taskrequirementsservicedto.dto.FileReqirement;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListOfRequirementsConverter implements Converter<String, List<FileReqirement>> {

    private final ObjectMapper objectMapper;

    public ListOfRequirementsConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<FileReqirement> convert(String source) {
        try {
            return objectMapper.readValue(source, new TypeReference<List<FileReqirement>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert requirements");
        }
    }
}
