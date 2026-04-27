package io.github.tessla2.libraryapi.service;


import io.github.tessla2.libraryapi.model.Book;
import io.github.tessla2.libraryapi.model.BookGenre;
import io.github.tessla2.libraryapi.model.User;
import io.github.tessla2.libraryapi.repository.BookRepository;
import io.github.tessla2.libraryapi.security.SecurityService;
import io.github.tessla2.libraryapi.validator.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static io.github.tessla2.libraryapi.repository.specs.BookSpecs.*;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final SecurityService securityService;

    private final BookValidator validator;

    public Book save(Book book) {
        validator.validate(book);
        User user = securityService.getLoggedUser();
        book.setUser(user);
        return repository.save(book);
    }

    public Optional<Book> getById(UUID id) {
        return repository.findById(id);
    }

    public void delete(Book book){
        repository.delete(book);
    }

    public Page<Book> search(
            String isbn,
            String title,
            String authorName,
            BookGenre genre,
            Integer publicationYear,
            Integer page,
            Integer pageSize) {
//        // select * from book where isbn = :isbn and authorName =
//        Specification<Book> specs =
//                BookSpecs.isbnEqual(isbn)
//                .and(BookSpecs.titleLike(title))
//                .and(BookSpecs.genreEqual(genre));

        // select * from book where 0 = 0
        Specification<Book> specs = ((root, query, cb) -> cb.conjunction());

        if(isbn != null){
            specs = specs.and(isbnEqual(isbn));
        }

        if(title != null) {
            specs = specs.and(titleLike(title));

        }
            if(genre != null){
                specs = specs.and(genreEqual(genre));

        }

            if(authorName != null) {
                specs = specs.and(nameAuthorLike(authorName));
            }


        Pageable pageRequest = PageRequest.of(page, pageSize);

        return repository.findAll(specs, pageRequest);
    }

    public void update(Book book) {
        if(book.getId() == null){
            throw new IllegalArgumentException("Book cannot be null for update.");

        }
        validator.validate(book);
        repository.save(book);
    }



}
