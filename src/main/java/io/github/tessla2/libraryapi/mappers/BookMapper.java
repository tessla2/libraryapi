package io.github.tessla2.libraryapi.mappers;

import io.github.tessla2.libraryapi.controller.dto.BookRegistrationDTO;
import io.github.tessla2.libraryapi.controller.dto.SearchBookDTO;
import io.github.tessla2.libraryapi.model.Book;
import io.github.tessla2.libraryapi.repository.AuthorRepository;
import jakarta.annotation.Nonnull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = AuthorMapper.class)
public abstract class BookMapper {


    @Autowired
    AuthorRepository authorRepository;



    @Mapping(target = "author", expression = "java( authorRepository.findById(dto.idAuthor()).orElse(null) )")
    public abstract  Book toEntity(BookRegistrationDTO dto);

    @Mapping(target = "author", expression = "java( authorRepository.findById(dto.idAuthor()).orElse(null) )")
    public abstract void updateEntity(BookRegistrationDTO dto, @MappingTarget @Nonnull Book book);

    public abstract SearchBookDTO toDTO(Book book);
}
