package io.github.tessla2.libraryapi.validator;


import io.github.tessla2.libraryapi.config.MessageService;
import io.github.tessla2.libraryapi.exceptions.DuplicateRecordException;
import io.github.tessla2.libraryapi.model.Author;
import io.github.tessla2.libraryapi.repository.AuthorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthorValidator {

    private AuthorRepository repository;
    private MessageService messageService;

    public AuthorValidator(AuthorRepository repository, MessageService messageService) {
        this.repository = repository;
        this.messageService = messageService;
    }

    public void validate(Author author){
        if(isDuplicate(author)){
            throw new DuplicateRecordException(messageService.getMessage("validation.author.duplicate",
                    author.getName()));
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

