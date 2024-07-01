package com.jilnash.studentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StudentDTO {

    @Size(message = "Student id must be positive integer", min = 1)
    private Long id;

    @Size(message = "User id must be positive integer", min = 1)
    private Long userId;

    @NotBlank(message = "Name is required")
    @Pattern(
            message = "Name should consist of latin letters only",
            regexp = "^[a-zA-Z]+$"
    )
    private String name;

    @NotBlank(message = "Surname is required")
    @Pattern(
            message = "Surname should consist of latin letters only",
            regexp = "^[a-zA-Z]+$"
    )
    private String surname;

    private String link;
}
