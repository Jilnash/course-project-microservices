package com.jilnash.hwresponseservice.mapper;

import com.jilnash.hwresponseservice.model.mongo.HwResponse;
import com.jilnash.hwresponseservicedto.dto.ResponseCreateDTO;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HwResponseMapper {

    public HwResponse toEntity(ResponseCreateDTO dto) {

        return HwResponse.builder()
                .id(dto.getId())
                .teacherId(dto.getTeacherId())
                .homeworkId(dto.getHwId())
                .isCorrect(dto.getIsCorrect())
                .comments(dto.getComments())
                .createdAt(new Date())
                .build();
    }
}
