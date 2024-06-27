package com.jilnash.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTO {

    private Long id;

    @NotEmpty(message = "Login cannot be empty")
    @NotNull(message = "Login cannot be null")
    private String login;

    @NotEmpty(message = "Email cannot be empty")
    @NotNull(message = "Email cannot be null")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid email")
    private String email;

    @NotEmpty(message = "Phone cannot be empty")
    @NotNull(message = "Phone cannot be null")
    @Pattern(regexp = "^\\d+$", message = "Invalid phone number")
    private String phone;
}
