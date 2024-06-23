package com.jilnash.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDTO {

    @NotEmpty(message = "Login cannot be empty")
    @NotNull(message = "Login cannot be null")
    private String login;

    @NotEmpty(message = "Email cannot be empty")
    @NotNull(message = "Email cannot be null")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid email")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @NotNull(message = "Password cannot be null")
    private String password;

    @NotEmpty(message = "Confirm password cannot be empty")
    @NotNull(message = "Confirm password cannot be null")
    private String confirmPassword;
}
