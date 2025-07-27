package com.jilnash.hwresponseservicedto.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonTypeName("audio")
public final class AudioFileComment extends Comments {

    @NotNull(message = "Text cannot be null")
    @NotEmpty(message = "Text cannot be empty")
    private String text;

    @NotNull(message = "From time cannot be null")
    @Min(value = 0, message = "From time cannot be less than 0")
    private Double fromTime;

    @NotNull(message = "To time cannot be null")
    @Min(value = 0, message = "To time cannot be less than 0")
    private Double toTime;
}
