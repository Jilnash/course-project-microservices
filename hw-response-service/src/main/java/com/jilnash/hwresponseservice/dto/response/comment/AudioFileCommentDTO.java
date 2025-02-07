package com.jilnash.hwresponseservice.dto.response.comment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class AudioFileCommentDTO extends FileCommentDTO {

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
