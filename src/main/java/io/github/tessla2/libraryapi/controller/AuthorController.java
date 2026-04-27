package io.github.tessla2.libraryapi.controller;


import io.github.tessla2.libraryapi.controller.dto.AuthorDTO;
import io.github.tessla2.libraryapi.mappers.AuthorMapper;
import io.github.tessla2.libraryapi.model.Author;
import io.github.tessla2.libraryapi.model.User;
import io.github.tessla2.libraryapi.security.SecurityService;
import io.github.tessla2.libraryapi.service.AuthorService;
import io.github.tessla2.libraryapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("authors")
@RequiredArgsConstructor
// http://localhost:8080/authors
public class AuthorController implements GenericController {

    private final AuthorService service;
    private final SecurityService securityService;
    private final AuthorMapper mapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')") //ResponseEntity is a generic type that represents the HTTP response //DTO is a data transfer object
    public ResponseEntity<Void> save(@RequestBody @Valid AuthorDTO dto) {//RequestBody to map the request body to the dto
        Author author = mapper.toEntity(dto);
        service.save(author);
        URI location = generateHeaderLocation(author.getId());

        return ResponseEntity.created(location).build(); //201 Created with location header
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<AuthorDTO> getDetails(@PathVariable("id") String id) {
        var idAuthor = UUID.fromString(id);

        return service
                .getFromId(idAuthor)
                .map(author -> {
                    AuthorDTO dto = mapper.toDTO(author);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //idempotent
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<Void> deleteAuthor(@PathVariable("id") String id) {

        var idAuthor = UUID.fromString(id);
        Optional<Author> authorOptional = service.getFromId(idAuthor);

        if (authorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteAuthor(authorOptional.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<List<AuthorDTO>> search(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "nationality", required = false) String nationality) {
        List<Author> result = service.searchByExample(name, nationality);
        List<AuthorDTO> list = result
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }


    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id,     //ResponseEntity is a generic type that represents the HTTP response
                                         @RequestBody @Valid AuthorDTO dto) //RequestBody to map the request body to the dto
    {
        var idAuthor = UUID.fromString(id);
        Optional<Author> authorOptional = service.getFromId(idAuthor);

        if (authorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var author = authorOptional.get();
        author.setName(dto.name());
        author.setNationality(dto.nationality());
        author.setBirthDate(dto.birth_date());

        service.update(author);

        return ResponseEntity.noContent().build(); //204 No Content

    }

}