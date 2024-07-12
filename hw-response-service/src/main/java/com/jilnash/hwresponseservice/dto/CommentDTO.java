package com.jilnash.hwresponseservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

@Value
public class CommentDTO implements Serializable {

    Long id;

    @NotNull(message = "Text should not be null")
    @NotBlank(message = "Text should not be blank")
    String text;

    @NotNull(message = "Time range should not be null")
    TimeRangeCreateDTO timeRange;
}