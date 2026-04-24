package io.github.tessla2.libraryapi.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationDTO(
        @NotBlank(message = "Required field")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Field exceeds the maximum allowed length")
        String email,
        @NotBlank(message = "Required field")
        @Size(min = 6, max = 100, message = "Field exceeds the maximum allowed length")
        String password,
        java.util.List<String> roles) {
}