package io.github.tessla2.libraryapi.controller.dto;


//DTO is to separate the view layer from the model layer, is the persistence layer
public record AuthorDTO(String name,
                        String nationality) {

}
