package io.github.tessla2.libraryapi.controller.dto;

import io.github.tessla2.libraryapi.model.Author;
import io.github.tessla2.libraryapi.model.BookGenre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BookRegistrationDTO(
        @ISBN
        @NotBlank(message = "Required field")
        String isbn,
        @NotBlank(message = "Required field")
        String title,
        @NotNull(message = "Required field")
        @Past(message = "Cannot use a future date")
        LocalDate publicationDate,
        BookGenre genre,
        BigDecimal price,
        @NotNull(message = "Required field")
        UUID idAuthor
) {
}
