package com.jilnash.hwresponseservicedto.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonTypeName("video")
public final class VideoFileComment extends Comments {


    @NotNull(message = "Line cannot be null")
    private String lineJson;

    @NotNull(message = "From time cannot be null")
    private String fromTime;

    @NotNull(message = "To time cannot be null")
    private String toTime;

    @NotNull(message = "Text cannot be null")
    private String text;
}
