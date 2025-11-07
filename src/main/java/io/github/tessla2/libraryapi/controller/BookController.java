package io.github.tessla2.libraryapi.controller;


import io.github.tessla2.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository repository;
}
