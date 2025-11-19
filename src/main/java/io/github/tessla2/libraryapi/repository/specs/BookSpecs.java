package io.github.tessla2.libraryapi.repository.specs;

import io.github.tessla2.libraryapi.model.Book;
import io.github.tessla2.libraryapi.model.BookGenre;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecs {

    public static Specification<Book> isbnEqual(String isbn) {
        return (root, query, cb) -> cb.equal(root.get("isbn"), isbn);
    }

    // upper(book.title) like %title%
    public static Specification<Book> titleLike(String title) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("title")), "%" + title.toUpperCase() + "%");
    }

    public static  Specification<Book> genreEqual(BookGenre genre) {
        return (root, query, cb) -> cb.equal(root.get("genre"), genre);
    }

    // and to_char(publication_date, 'YYYY') = :publicationYear
    public static  Specification<Book> publicationYearEqual(Integer publicationYear) {
        return (root, query, cb) ->
                cb.equal( cb.function("to_char", String.class, root.get("publication_date")) ,publicationYear.toString());
    }

    public static  Specification<Book> nameAuthorLike(String name) {
        return (root, query, cb) -> {
            Join<Object, Object> joinAuthor = root.join("author", JoinType.LEFT);
            return cb.like( cb.upper(joinAuthor.get("name")), "%" + name.toUpperCase() + "%");


//            return cb.like(cb.upper(root.get("author").get("name")), "%" + name.toUpperCase() + "%");
        };

    }

}
