package com.jilnash.hwresponseservice.dto.response.comment;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class VideoFileCommentDTO extends FileCommentDTO {

    @NotNull(message = "Line cannot be null")
    private String lineJson;

    @NotNull(message = "From time cannot be null")
    private String fromTime;

    @NotNull(message = "To time cannot be null")
    private String toTime;

    @NotNull(message = "Text cannot be null")
    private String text;
}
