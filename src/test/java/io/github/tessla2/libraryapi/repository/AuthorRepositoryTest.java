package io.github.tessla2.libraryapi.repository;

import io.github.tessla2.libraryapi.model.Author;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@SpringBootTest
public class AuthorRepositoryTest {


    @Autowired
    AuthorRepository repository;

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
        var id = UUID.fromString("2b836311-921d-4a0a-9d64-5428ef1fec9f");
        var maria = repository.findById(id).get();
        repository.delete(maria);
    }

    }
