package com.jilnash.teacherservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherUpdateDTO {

    private Long id;

    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name should not be empty")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must contain only letters")
    private String name;

    @NotNull(message = "Surname is required")
    @NotEmpty(message = "Surname should not be empty")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Surname must contain only letters")
    private String surname;

    @NotNull(message = "Description is required")
    @Size(min = 20, message = "Description must be at least 10 characters long")
    private String description;

    private String photo;

    private String mediaLink;
}
