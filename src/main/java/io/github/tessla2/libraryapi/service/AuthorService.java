package io.github.tessla2.libraryapi.service;


import io.github.tessla2.libraryapi.controller.dto.validator.AuthorValidator;
import io.github.tessla2.libraryapi.exceptions.OperationNotAllowed;
import io.github.tessla2.libraryapi.model.Author;
import io.github.tessla2.libraryapi.repository.AuthorRepository;
import io.github.tessla2.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor // Using Lombok to generate constructor with required arguments
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorValidator validator;
    private final BookRepository bookRepository;

    public Author save(Author author){
        validator.validate(author);
        return authorRepository.save(author);
    }

    public Author update(Author author){
        if(author.getId() == null){
            throw new IllegalArgumentException("Author ID cannot be null for update.");
        }
        validator.validate(author);
        return authorRepository.save(author);
    }

    public Optional<Author> getFromId(UUID id){
        return authorRepository.findById(id);
    }

    public void deleteAuthor(Author author){
        if(hasBooks(author)){
            throw new OperationNotAllowed("Author with associated books cannot be deleted.");
        }
        authorRepository.delete(author);
    }

    public List<Author> search (String name, String nationality){
        if(name != null && nationality != null){
            return authorRepository.findByNameAndNationality(name, nationality);
        }

        if(name != null){
            return authorRepository.findByName(name);
        }
        if(nationality != null){
            return authorRepository.findByNationality(nationality);
    }
        return authorRepository.findAll();
    }
    public List<Author> searchByExample(String name, String nationality){
        var author = new Author();
        author.setName(name);
        author.setNationality(nationality);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Author> authorExample = Example.of(author, matcher);
        return authorRepository.findAll(authorExample);

    }


    public boolean hasBooks(Author author){
        return bookRepository.existsByAuthor(author);
    }

}
