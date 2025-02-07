package com.jilnash.hwresponseservice.dto.response.comment;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ImageFileCommentDTO extends FileCommentDTO {

    @NotNull(message = "Line cannot be null")
    private String lineJson;

    @NotNull(message = "Column cannot be null")
    private String text;
}
