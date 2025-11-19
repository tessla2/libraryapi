package io.github.tessla2.libraryapi.repository;

import io.github.tessla2.libraryapi.model.Author;
import io.github.tessla2.libraryapi.model.Book;
import io.github.tessla2.libraryapi.model.BookGenre;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {


    Page<Book> findByAuthor(Author author, Pageable pegable);
    // select * from book where author_id = id
    List<Book> findByAuthor(Author author); //method to find books by author

    List<Book> findByTitle (String title); //method to find books by title

    Optional<Book> findByIsbn(String isbn);

    boolean existsByAuthor(Author author); //method to check if books exist by author

   @Query("select b from Book as b order by b.title, b.price")
    List<Book> findAllOrderByTitleAndPrice();

    @Query("""
    select b.genre
    from Book b
    join b.author a
            where a.nationality = 'British'
    order by b.genre
    """)
    List<String> findBooksAuthorNationalityBritish();

    @Query("select b from Book b where b.genre =:genre order by :paramOrdnance")
    List<Book> findByGenre(
            @Param("genre") BookGenre bookGenre,
            @Param("paramOrdnance") String nameProperty);

    // positional parameters
    @Query("select b from Book b where b.genre = ?2 order by ?1 ")
    List<Book> findByGenrePositionalParameters(String nameProperty, BookGenre bookGenre);


    // @Modifying indicates this query changes data (DELETE, UPDATE, etc.)
    // @Transactional ensures this operation runs inside a transaction.
    @Modifying
    @Transactional
    @Query( "delete from Book where genre = ?1")
    void deleteBooksByGenre(BookGenre genre);

    @Modifying
    @Transactional
    @Query(" update Book set publicationDate = ?1 ")
    void updatePublicationDate(LocalDate newDate);
}
