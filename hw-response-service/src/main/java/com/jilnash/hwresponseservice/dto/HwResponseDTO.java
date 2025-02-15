package com.jilnash.hwresponseservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class HwResponseDTO implements Serializable {

    Long id;

    String teacherId;

    @NotNull(message = "Homework id should not be null")
    String homeworkId;

    @NotNull(message = "Comments should not be null")
    @NotEmpty(message = "Comments should not be empty")
    List<@Valid CommentDTO> comments;

    @NotNull(message = "Is correct should not be null")
    Boolean isCorrect;
}