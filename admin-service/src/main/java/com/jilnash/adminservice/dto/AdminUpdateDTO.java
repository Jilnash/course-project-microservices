package com.jilnash.adminservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateDTO {

    @NotNull(message = "Id is required")
    private Long id;

    private Long userId;

    @NotNull(message = "Name is required")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name must contain only letters")
    private String name;

    @NotNull(message = "Surname is required")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Surname                                                                              must contain only letters")
    private String surname;
}
