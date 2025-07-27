package com.jilnash.hwresponseservicedto.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonTypeName("image")
public final class ImageFileComment extends Comments {

    @NotNull(message = "Line cannot be null")
    private String lineJson;

    @NotNull(message = "Column cannot be null")
    private String text;
}
