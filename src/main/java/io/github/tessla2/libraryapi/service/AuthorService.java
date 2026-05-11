package io.github.tessla2.libraryapi.service;


import io.github.tessla2.libraryapi.model.User;
import io.github.tessla2.libraryapi.security.SecurityService;
import io.github.tessla2.libraryapi.validator.AuthorValidator;
import io.github.tessla2.libraryapi.exceptions.OperationNotAllowed;
import io.github.tessla2.libraryapi.model.Author;
import io.github.tessla2.libraryapi.repository.AuthorRepository;
import io.github.tessla2.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorValidator validator;
    private final BookRepository bookRepository;
    private final SecurityService securityService;


    @Transactional
    public Author save(Author author){
        validator.validate(author);
        User user = securityService.getLoggedUser();
        author.setUser(user);
        return authorRepository.save(author);
    }

    @Transactional
    public Author update(Author author){
        if(author.getId() == null){
            throw new IllegalArgumentException("Author ID cannot be null for update.");
        }
        validator.validate(author);
        return authorRepository.save(author);
    }

    @Transactional(readOnly = true)
    public Optional<Author> getFromId(UUID id){
        return authorRepository.findById(id);
    }

    @Transactional
    public void deleteAuthor(Author author){
        if(hasBooks(author)){
            throw new OperationNotAllowed("Author with associated books cannot be deleted.");
        }
        authorRepository.delete(author);
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    // Busca autor por nome exato (primeiro resultado)
    public Optional<Author> findByNameExact(String name) {
        var authors = authorRepository.findByName(name);
        return authors.stream().findFirst();
    }

    // Cria um autor minimo (apenas nome) para importacao automatica
    @Transactional
    public Author createMinimal(String name) {
        var author = new Author();
        author.setName(name);
        author.setNationality("Unknown");
        User user = securityService.getLoggedUser();
        author.setUser(user);
        var saved = authorRepository.save(author);
        log.info("Author auto-created from Google Books import: name='{}'", name);
        return saved;
    }
}
