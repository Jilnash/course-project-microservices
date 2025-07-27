package com.jilnash.hwresponseservicedto.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ImageFileComment.class, name = "image"),
        @JsonSubTypes.Type(value = AudioFileComment.class, name = "audio"),
        @JsonSubTypes.Type(value = VideoFileComment.class, name = "video")
})
@Getter
@Setter
public sealed class Comments permits ImageFileComment, AudioFileComment, VideoFileComment {

    @NotNull(message = "Filename cannot be null")
    @NotBlank(message = "Filename cannot be blank")
    private String fileName;
}
