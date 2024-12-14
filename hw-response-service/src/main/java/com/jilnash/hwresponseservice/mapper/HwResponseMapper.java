package com.jilnash.hwresponseservice.mapper;

import com.jilnash.hwresponseservice.dto.HwResponseDTO;
import com.jilnash.hwresponseservice.model.Comment;
import com.jilnash.hwresponseservice.model.HwResponse;
import com.jilnash.hwresponseservice.model.TimeRange;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class HwResponseMapper {

    public HwResponse toEntity(HwResponseDTO dto) {

        return HwResponse.builder()
                .id(dto.getId())
                .teacherId(dto.getTeacherId())
                .homeworkId(dto.getHomeworkId())
                .isCorrect(dto.getIsCorrect())
                .comments(dto.getComments().stream().map(commentDTO -> Comment.builder()
                                .id(commentDTO.getId())
                                .text(commentDTO.getText())
                                .timeRange(TimeRange.builder()
                                        .id(commentDTO.getTimeRange().getId())
                                        .start(commentDTO.getTimeRange().getStart())
                                        .end(commentDTO.getTimeRange().getEnd())
                                        .build())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
