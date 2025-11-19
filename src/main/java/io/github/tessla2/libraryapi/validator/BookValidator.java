package io.github.tessla2.libraryapi.validator;


import io.github.tessla2.libraryapi.exceptions.DuplicateRecordException;
import io.github.tessla2.libraryapi.exceptions.InvalidComponentException;
import io.github.tessla2.libraryapi.model.Book;
import io.github.tessla2.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookValidator {

    private static final int PUBLICATION_YEAR_THRESHOLD = 2020;

    private final BookRepository repository;

    public void validate(Book book) {
        if (existsByIsbn(book)) {
            throw new DuplicateRecordException("Book with ISBN already exists.");
        }

        if(isRequiredPriceNull(book)){
            throw new InvalidComponentException("price", "Price required for books with publication date above 2020." );
        }
    }

    private boolean isRequiredPriceNull(Book book) {
        return book.getPrice() == null &&
                book.getPublicationDate().getYear() >= PUBLICATION_YEAR_THRESHOLD;
    }


    private boolean existsByIsbn(Book book) {
        Optional<Book> bookFound = repository.findByIsbn(book.getIsbn());

        if(book.getId() == null){
                return bookFound.isPresent();
        }

        return bookFound
                .map(Book::getId)
                .stream()
                .anyMatch(id -> !id.equals(book.getId()));
    }
}
