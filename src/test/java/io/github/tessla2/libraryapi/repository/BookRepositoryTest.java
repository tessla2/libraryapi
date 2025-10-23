package io.github.tessla2.libraryapi.repository;

import io.github.tessla2.libraryapi.model.Author;
import io.github.tessla2.libraryapi.model.Book;
import io.github.tessla2.libraryapi.model.BookGenre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class BookRepositoryTest {

    @Autowired
    BookRepository repository;

    @Autowired
    AuthorRepository authorRepository;

    @Test
    void saveTest() {
        Book book = new Book();
        book.setTitle("Don Quixote");
        book.setIsbn("978-3-16-148410-0");
        book.setPreco(BigDecimal.valueOf(13.99));
        book.setGenre(BookGenre.FICCAO_CIENTIFICA);
        book.setPublicationDate(LocalDate.of(1980, 5, 20));


        Author author = authorRepository
                .findById(UUID.fromString("18d22ae9-f1da-428b-a905-7b3fdbbe3acd"))
                .orElse(null);

        book.setAuthor(author);

        repository.save(book);
    }


}