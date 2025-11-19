package io.github.tessla2.libraryapi.controller;


import io.github.tessla2.libraryapi.controller.dto.BookRegistrationDTO;
import io.github.tessla2.libraryapi.controller.dto.SearchBookDTO;
import io.github.tessla2.libraryapi.controller.mappers.BookMapper;
import io.github.tessla2.libraryapi.model.Book;
import io.github.tessla2.libraryapi.model.BookGenre;
import io.github.tessla2.libraryapi.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("books")

@RequiredArgsConstructor
public class BookController implements GenericController{

    private final BookService service;
    private final BookMapper mapper;


    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid BookRegistrationDTO dto) {
            Book book = mapper.toEntity(dto);
            //send entity to service to validate and save on database
            service.save(book);
            //return code with header location
            var url = generateHeaderLocation(book.getId());
            return ResponseEntity.created(url).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<SearchBookDTO> getBooks(@PathVariable("id") String id) {
        return service.getById(UUID.fromString(id))
                .map(book -> {
                    var dto = mapper.toDTO(book);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") String id){
        return service.getById(UUID.fromString(id))
                .map(book -> {
                    service.delete(book);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<SearchBookDTO>> search(
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author-name", required = false) String authorName,
            @RequestParam(value = "genre", required = false) BookGenre genre,
            @RequestParam(value = "publication-year", required = false) Integer publicationYear,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "10") Integer pageSize
    ){
        Page<Book> pageResult = service.search(
                isbn, title, authorName, genre, publicationYear, page, pageSize);

        Page<SearchBookDTO> result = pageResult.map(mapper::toDTO);

        return ResponseEntity.ok(result);

    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id,
                                       @RequestBody @Valid BookRegistrationDTO dto) {
        return service.getById(UUID.fromString(id))
                .map(book ->{
                    Book entityAux = mapper.toEntity(dto);
                    book.setPublicationDate(entityAux.getPublicationDate());
                    book.setIsbn(entityAux.getIsbn());
                    book.setPrice(entityAux.getPrice());
                    book.setTitle(entityAux.getTitle());
                    book.setGenre(entityAux.getGenre());
                    book.setAuthor(entityAux.getAuthor());
                    service.update(book);

                    return ResponseEntity.noContent().build();

                }).orElseGet( () -> ResponseEntity.notFound().build());
    }


}
