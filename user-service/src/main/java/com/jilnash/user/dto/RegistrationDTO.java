package com.jilnash.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrationDTO {

    @NotNull(message = "Login cannot be null")
    @NotEmpty(message = "Login cannot be empty")
    private String login;

    @NotNull(message = "Email cannot be null")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Phone cannot be null")
    @NotEmpty(message = "Phone cannot be null")
    private String phone;

    @NotNull(message = "Password cannot be null")
    @NotEmpty(message = "Password cannot be null")
    private String password;

    @NotNull(message = "Confirm password cannot be null")
    @NotEmpty(message = "Confirm password cannot be null")
    private String confirmPassword;
}
