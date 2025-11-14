package io.github.tessla2.libraryapi.controller.dto.validator;


import io.github.tessla2.libraryapi.exceptions.DuplicateRecordException;
import io.github.tessla2.libraryapi.model.Author;
import io.github.tessla2.libraryapi.repository.AuthorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthorValidator {

    private AuthorRepository repository;

    public AuthorValidator(AuthorRepository repository) {
        this.repository = repository;
    }

    public void validate(Author author){
        if(isDuplicate(author)){
            throw new DuplicateRecordException("Author with name '" + author.getName() + "' already exists.");
        }
    }

    private boolean isDuplicate(Author author){
        Optional<Author> existingAuthor = repository.findByNameAndNationalityAndBirthDate(
                author.getName(),
                author.getNationality(),
                author.getBirthDate()
        );

        if(author.getId() == null){
            return existingAuthor.isPresent();
        }
        return !author.getId().equals(existingAuthor.get().getId()) && existingAuthor.isPresent();
        }
    }

