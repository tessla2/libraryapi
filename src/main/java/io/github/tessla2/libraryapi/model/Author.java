package io.github.tessla2.libraryapi.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Table(name = "author", schema = "public")

public class Author {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;


    @Column(name = "nationality", length = 50, nullable = false)
    private String nationality;

   // @OneToMany(mappedBy = "author")
    @Transient // To avoid loading books in this example
    private List<Book> books;

}
