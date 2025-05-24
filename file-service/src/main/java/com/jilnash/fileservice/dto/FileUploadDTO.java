package com.jilnash.fileservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record FileUploadDTO(
        @NotNull(message = "Bucket should not be null")
        @NotEmpty(message = "Bucket should not be empty")
        String bucket,
        @NotNull(message = "File name should not be null")
        @NotEmpty(message = "File name should not be empty")
        String fileName,
        @NotNull(message = "Files should not be null")
        @Size(min = 1, message = "Files should not be empty")
        List<MultipartFile> files) {
}
