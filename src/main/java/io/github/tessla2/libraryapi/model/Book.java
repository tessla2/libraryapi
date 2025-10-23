package io.github.tessla2.libraryapi.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "book", schema = "public")
@Data
public class Book {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isbn", length = 20, nullable = false)
    private String isbn;

    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre", length = 30)
    private BookGenre genre;

    @Column(name = "preco", precision = 18, scale = 2)
    private BigDecimal preco;
   // private BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = "id_author")
    private Author author;

}
