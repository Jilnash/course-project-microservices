package com.jilnash.hwresponseservice.mapper;

import com.jilnash.hwresponseservice.dto.response.HwResponseDTO;
import com.jilnash.hwresponseservice.model.mongo.HwResponse;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HwResponseMapper {

    public HwResponse toEntity(HwResponseDTO dto) {

        return HwResponse.builder()
                .id(dto.getId())
                .teacherId(dto.getTeacherId())
                .homeworkId(dto.getHomeworkId())
                .isCorrect(dto.getIsCorrect())
                .comments(dto.getComments())
                .createdAt(dto.getCreatedAt() == null ? new Date() : dto.getCreatedAt())
                .build();
    }
}
