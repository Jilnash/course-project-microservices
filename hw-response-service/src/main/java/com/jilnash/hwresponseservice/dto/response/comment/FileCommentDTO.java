package com.jilnash.hwresponseservice.dto.response.comment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ImageFileCommentDTO.class, name = "image"),
        @JsonSubTypes.Type(value = AudioFileCommentDTO.class, name = "audio"),
        @JsonSubTypes.Type(value = VideoFileCommentDTO.class, name = "video")
})
abstract public class FileCommentDTO {

    @NotNull(message = "File name cannot be null")
    @NotEmpty(message = "File name cannot be empty")
    private String fileName;
}
