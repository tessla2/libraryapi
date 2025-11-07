package io.github.tessla2.libraryapi.controller.dto;

import io.github.tessla2.libraryapi.model.Author;
import io.github.tessla2.libraryapi.model.BookGenre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BookRegistrationDTO(String isbn,
                                  String title,
                                  LocalDate publicationDate,
                                  BookGenre genre,
                                  BigDecimal price,
                                  UUID idAuthor
                                  ) {
}
