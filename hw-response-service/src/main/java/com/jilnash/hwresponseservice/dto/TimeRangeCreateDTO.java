package com.jilnash.hwresponseservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.io.Serializable;

@Value
public class TimeRangeCreateDTO implements Serializable {

    Long id;

    @NotNull(message = "Start should not be null")
    @Positive(message = "Start should be greater than 0")
    Double start;

    @NotNull(message = "End should not be null")
    @Positive(message = "End should be greater than 0")
    Double end;
}