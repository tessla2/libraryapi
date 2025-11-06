package io.github.tessla2.libraryapi.controller;


import io.github.tessla2.libraryapi.controller.dto.AuthorDTO;
import io.github.tessla2.libraryapi.controller.dto.ErrorResponse;
import io.github.tessla2.libraryapi.exceptions.DuplicateRecordException;
import io.github.tessla2.libraryapi.exceptions.OperationNotAllowed;
import io.github.tessla2.libraryapi.model.Author;
import io.github.tessla2.libraryapi.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("authors")
@RequiredArgsConstructor
    // http://localhost:8080/authors
public class AuthorController {

    private final AuthorService service;


    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid AuthorDTO author) { //ResponseEntity is a generic type that represents the HTTP response
        try {                                                                  //DTO is a data transfer object
            Author authorEntity = author.mapToAuthor();
            service.save(authorEntity);

            // http://localhost:8080/authors/78521941-95c8-4a39-a33d-99cf13b34c57
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(authorEntity.getId())
                    .toUri();

            return ResponseEntity.created(location).build(); //201 Created with location header

        } catch (DuplicateRecordException e) {
            var errorDTO = ErrorResponse.conflictError(e.getMessage());
           return ResponseEntity.status(errorDTO.status()).body(errorDTO);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<AuthorDTO> getDetails(@PathVariable("id") String id){
        var idAuthor = UUID.fromString(id);
        Optional<Author> authorOptional = service.getFromId(idAuthor);
        if(authorOptional.isPresent()){
            Author author = authorOptional.get();
            AuthorDTO dto = new AuthorDTO(author.getId(), author.getName(),
                    author.getNationality(), author.getBirthdate());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();

    }

    //idempotent
    @DeleteMapping("{id}")
        public ResponseEntity<Object> deleteAuthor (@PathVariable("id") String id){
        try
        {
        var idAuthor = UUID.fromString(id);
        Optional<Author> authorOptional = service.getFromId(idAuthor);

        if (authorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteAuthor(authorOptional.get());
        return ResponseEntity.noContent().build();
    } catch(OperationNotAllowed e) {
            var errorResponse = ErrorResponse.standardError(e.getMessage());
            return ResponseEntity.status(errorResponse.status()).body(errorResponse);
    }
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> search(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "nationality", required = false) String nationality){
        List<Author> result = service.searchByExample(name, nationality);
        List<AuthorDTO> list = result.stream().map(author -> new AuthorDTO(author.getId(),
                author.getName(), author.getNationality(),
                author.getBirthdate())).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }


    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id,     //ResponseEntity is a generic type that represents the HTTP response
                                             @RequestBody @Valid AuthorDTO dto)       //RequestBody to map the request body to the dto
        {
            try{
        var idAuthor = UUID.fromString(id);
        Optional<Author> authorOptional = service.getFromId(idAuthor);

        if(authorOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var author = authorOptional.get();
        author.setName(dto.name());
        author.setNationality(dto.nationality());
        author.setBirthdate(dto.birth_date());

        service.update(author);

        return ResponseEntity.noContent().build(); //204 No Content
        }catch (DuplicateRecordException e){
            var errorDTO = ErrorResponse.conflictError(e.getMessage());
            return ResponseEntity.status(errorDTO.status()).body(errorDTO);
        }

        }

}