package io.github.tessla2.libraryapi.repository;

import io.github.tessla2.libraryapi.model.Author;
import io.github.tessla2.libraryapi.model.Book;
import io.github.tessla2.libraryapi.model.BookGenre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

        book.setAuthor(new Author());

        repository.save(book);
    }



    @Test
    void saveCascadeTest() {
        Book book = new Book();
        book.setTitle("Don Quixote");
        book.setIsbn("978-3-16-148410-0");
        book.setPreco(BigDecimal.valueOf(13.99));
        book.setGenre(BookGenre.FICCAO_CIENTIFICA);
        book.setPublicationDate(LocalDate.of(1980, 5, 20));


        Author author = new Author();
        author.setName("Miguel de Cervantes");
        author.setNationality("Spanish");


        book.setAuthor(author);

        repository.save(book);
    }

    @Test
    void deleteAuthor(){
        var id = UUID.fromString("10019f86-38e2-4609-861a-206cd1385db0");
        var author = repository.findById(id).get();
        repository.delete(author);
    }



    @Test
    void updateBook() { //update book's author
        UUID id = UUID.fromString("c4f6e2d3-F153-4b41-ABA4-F31494D733E7");
        var bookToUpdate = repository
                .findById(id).orElse(null);

        UUID idAuthor = UUID.fromString("18d22ae9-f1da-428b-a905-7b3fdbbe3acd");

        Author Lourenço = authorRepository.findById(idAuthor).orElse(null);

        bookToUpdate.setAuthor(Lourenço);
        repository.save(bookToUpdate);


    }
        @Test
        void listBookTest(){
            UUID id = UUID.fromString("c4f6e2d3-F");
            Book book = repository.findById(id).orElse(null);
            System.out.println("Book: ");
            System.out.println(book.getTitle());
            System.out.println("Author: ");
            System.out.println(book.getAuthor().getName());
        }

        @Test
    void findByTitleTest(){ //find book by title
        List<Book> book = repository.findByTitle("Harry Potter and the Philosopher's Stone");
        book.forEach(System.out::println); //print each book found
        }


        /*
    @Test
    void listBooksWithJPQLQuery() {
        var result = repository.listAllOrderedByTitleAndPrice();
        result.forEach(System.out::println);
    }

    @Test
    void listAuthorsOfBooks() {
        var result = repository.listAuthorsOfBooks();
        result.forEach(System.out::println);
    }

    @Test
    void listDistinctBookTitles() {
        var result = repository.listDistinctBookNames();
        result.forEach(System.out::println);
    }

    @Test
    void listGenresOfBrazilianAuthorsBooks() {
        var result = repository.listGenresOfBrazilianAuthors();
        result.forEach(System.out::println);
    }

    @Test
    void listByGenreWithQueryParamTest() {
        var result = repository.findByGenre(BookGenre.MYSTERY, "price");
        result.forEach(System.out::println);
    }

    @Test
    void listByGenreWithPositionalParamTest() {
        var result = repository.findByGenrePositionalParameters("price", BookGenre.MYSTERY);
        result.forEach(System.out::println);
    }

    @Test
    void deleteByGenreTest() {
        repository.deleteByGenre(BookGenre.SCIENCE);
    }

    @Test
    void updatePublicationDateTest() {
        repository.updatePublicationDate(LocalDate.of(2000, 1, 1));
    }
    */
}