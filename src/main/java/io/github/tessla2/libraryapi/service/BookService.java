package io.github.tessla2.libraryapi.service;


import io.github.tessla2.libraryapi.model.Book;
import io.github.tessla2.libraryapi.model.BookGenre;
import io.github.tessla2.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.github.tessla2.libraryapi.repository.specs.BookSpecs.*;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;

    public Book save(Book book) {
        return repository.save(book);
    }

    public Optional<Book> getById(UUID id) {
        return repository.findById(id);
    }

    public void delete(Book book){
        repository.delete(book);
    }

    public List<Book> search(
            String isbn,
            String title,
            String authorName,
            BookGenre genre,
            Integer publicationYear)
    {
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

        if(title != null){
            specs = specs.and(titleLike(title));

            if(genre != null){
                specs = specs.and(genreEqual(genre));
            }
        }

        return repository.findAll(specs);
    }
}
