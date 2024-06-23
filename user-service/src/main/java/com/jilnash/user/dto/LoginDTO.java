package com.jilnash.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDTO {

    @NotNull(message = "Login or email cannot be null")
    @NotEmpty(message = "Login or email cannot be empty")
    private String loginOrEmail;

    @NotNull(message = "Password cannot be null")
    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
