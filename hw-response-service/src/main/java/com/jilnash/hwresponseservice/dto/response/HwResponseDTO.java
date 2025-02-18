package com.jilnash.hwresponseservice.dto.response;

import com.jilnash.hwresponseservice.dto.response.comment.FileCommentDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HwResponseDTO {

    String id;

    String teacherId;

    @NotNull(message = "Homework id should not be null")
    String homeworkId;

    @NotNull(message = "Comments should not be null")
    @NotEmpty(message = "Comments should not be empty")
    List<@Valid FileCommentDTO> comments;

    @NotNull(message = "Is correct should not be null")
    Boolean isCorrect;
}