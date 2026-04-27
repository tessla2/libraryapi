package io.github.tessla2.libraryapi.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UserDTO(
        UUID id,
        @NotBlank(message = "Required field")
        @Size(min = 3, max = 50, message = "Field exceeds the maximum allowed length")
        String login,
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Field exceeds the maximum allowed length")
        String email,
        @NotBlank(message = "Required field")
        @Size(min = 6, max = 100, message = "Field exceeds the maximum allowed length")
        String password,
        java.util.List<String> roles) {
}