package io.github.tessla2.libraryapi.service;

import io.github.tessla2.libraryapi.model.Author;
import io.github.tessla2.libraryapi.model.Book;
import io.github.tessla2.libraryapi.model.BookGenre;
import io.github.tessla2.libraryapi.repository.AuthorRepository;
import io.github.tessla2.libraryapi.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


public class TransactionService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;



    @Transactional
    public void updateWithoutSave(){
        var book = bookRepository
                .findById(UUID.fromString("d3b5f4e2-5f4e-4c3a-9f2e-1c2b3a4d5e6f"))
                .orElse(null);

        book.setPublicationDate(LocalDate.of(2025,6,1));

    }



    @Transactional
    public void execute() {

        Author author = new Author();
        author.setName("Miguel de Cervantes");
        author.setNationality("Spanish");


        authorRepository.save(author);

        if(author.getName().equals("Miguel de Cervantes")){
            throw new RuntimeException("Simulated exception to trigger rollback");
        }

        Book book = new Book();
        book.setTitle("Don Quixote");
        book.setIsbn("978-3-16-148410-0");
        book.setPrice(BigDecimal.valueOf(13.99));
        book.setGenre(BookGenre.SCIENCE_FICTION);
        book.setPublicationDate(LocalDate.of(1980, 5, 20));


        book.setAuthor(author);
        bookRepository.save(book);
    }
}
