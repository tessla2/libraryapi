package io.github.tessla2.libraryapi.controller;


import io.github.tessla2.libraryapi.controller.dto.AuthorDTO;
import io.github.tessla2.libraryapi.model.Author;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
// http://localhost:8080/authors
public class AuthorController {


    @PostMapping
    public ResponseEntity save(@RequestBody AuthorDTO author){ //ResponseEntity is a generic type that represents the HTTP response
                                                               //DTO is a data transfer object
        return new ResponseEntity("Author saved successfully" +  author, HttpStatus.CREATED); //201 Created
    }


}
