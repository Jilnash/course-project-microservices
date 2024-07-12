package com.jilnash.hwresponseservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
public class HwResponseDTO implements Serializable {

    Long id;

    @NotNull(message = "Teacher id should not be null")
    @Positive(message = "Teacher id should be greater than 0")
    Long teacherId;

    @NotNull(message = "Homework id should not be null")
    @Positive(message = "Homework id should be greater than 0")
    Long homeworkId;

    @NotNull(message = "Comments should not be null")
    @NotEmpty(message = "Comments should not be empty")
    List<@Valid CommentDTO> comments;
}