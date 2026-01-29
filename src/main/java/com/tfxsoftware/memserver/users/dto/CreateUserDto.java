package com.tfxsoftware.memserver.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public class CreateUserDto {

    @Getter
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Getter
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
    private String username;

    @Getter
    private String hashedPassword;

    // Getters and Setters
}
