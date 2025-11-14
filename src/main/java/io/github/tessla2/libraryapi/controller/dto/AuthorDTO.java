package io.github.tessla2.libraryapi.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

//DTO is to separate the view layer from the model layer, is the persistence layer
public record AuthorDTO(
        UUID id,
        @NotBlank(message = "Required field")
        @Size(min= 2, max = 100, message = "Field exceeds the maximum allowed length")
        String name,
        @NotBlank(message = "Required field")
        @Size(min = 2, max = 50, message = "Field exceeds the maximum allowed length")
        String nationality,
        @NotNull(message = "Required field")
        @Past(message = "Birth date cannot be in the future.")
        LocalDate birth_date) {
}
