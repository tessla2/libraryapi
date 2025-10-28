package io.github.tessla2.libraryapi.repository;

import io.github.tessla2.libraryapi.model.Author;
import io.github.tessla2.libraryapi.model.Book;
import io.github.tessla2.libraryapi.model.BookGenre;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@SpringBootTest
public class AuthorRepositoryTest {


    @Autowired
    AuthorRepository repository;

    @Autowired
    BookRepository bookRepository;

//    @Test
//    public void saveTest() {
//            Author author = new Author();
//            author.setName("Thomas Hardy");
//            author.setNationality("British");
//            var authorSave = repository.save(author);
//
//
//            System.out.println("Author saved: " + authorSave);
//        }

        @Test
    public void updateTest(){
            var id = UUID.fromString("452336da-b153-4b41-aba4-f31494d733e7");
            Optional<Author> optionalAuthor = repository.findById(id);
            if(optionalAuthor.isPresent()){
                Author foundAuthor = optionalAuthor.get();
                System.out.println("Author found: " + optionalAuthor);

                foundAuthor.setNationality("Chilean");
                repository.save(foundAuthor);
            }

        }
        @Test
    public void listarTest(){
            List<Author> list = repository.findAll();
            list.forEach(System.out::println);
        }

        @Test
    public void countTest(){
            System.out.println("Cont of authors: " + repository.count());
        }

        @Test
    public void deleteIdTest(){
            var id = UUID.fromString("452336da-b153-4b41-aba4-f31494d733e7");
            repository.deleteById(id);
        }

    @Test
    public void deleteTest(){
        var id = UUID.fromString("78521941-95c8-4a39-a33d-99cf13b34c57");
        var maria = repository.findById(id).get();
        repository.delete(maria);
    }





    @Test
    void saveAuthorWithBooksTest(){
        Author author = new Author();
        author.setName("J.K. Rowling");
        author.setNationality("British");

        Book book1 = new Book();
        book1.setTitle("Harry Potter and the Philosopher's Stone");
        book1.setIsbn("978-3-16-148410-0");
        book1.setPreco(BigDecimal.valueOf(10.99));
        book1.setGenre(BookGenre.FANTASIA);
        book1.setPublicationDate(LocalDate.of(1997, 6, 26));
        book1.setAuthor(author);

        Book book2 = new Book();
        book2.setTitle("Harry Potter and the Chamber of Secrets");
        book2.setIsbn("978-3-16-148411-7");
        book2.setPreco(BigDecimal.valueOf(11.99));
        book2.setGenre(BookGenre.FANTASIA);
        book2.setPublicationDate(LocalDate.of(1998, 7, 2));
        book2.setAuthor(author);


        author.setBooks(new ArrayList<>());
        author.getBooks().add(book1);
        author.getBooks().add(book2);

        repository.save(author);

        bookRepository.saveAll(author.getBooks());
    }

    @Test
    // @Transactional //
    void listAuthorBooks(){
            var id = UUID.fromString("10019f86-38e2-4609-861a-206cd1385db0");
            var author = repository.findById(id).get();

        List<Book> booksList = bookRepository.findByAuthor(author);
        author.setBooks(booksList);


        author.getBooks().forEach(System.out::println);

    }

    }
