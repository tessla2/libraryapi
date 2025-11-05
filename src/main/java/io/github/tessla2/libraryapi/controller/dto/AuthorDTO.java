package io.github.tessla2.libraryapi.controller.dto;


import io.github.tessla2.libraryapi.model.Author;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

//DTO is to separate the view layer from the model layer, is the persistence layer
public record AuthorDTO(
        UUID id,
        String name,
        String nationality,
        LocalDate birth_date) {


    // Mapping method to convert AuthorDTO to Author entity
    public Author mapToAuthor(){
        Author author = new Author();
        author.setId(this.id);
        author.setName(this.name);
        author.setNationality(this.nationality);
        author.setBirthdate(this.birth_date);
        return author;
    }

}
