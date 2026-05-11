package io.github.tessla2.libraryapi.controller;


import io.github.tessla2.libraryapi.controller.dto.UserDTO;
import io.github.tessla2.libraryapi.mappers.UserMapper;
import io.github.tessla2.libraryapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid UserDTO userDTO) {
        var user = mapper.toEntity(userDTO);
        service.save(user);
        return ResponseEntity.created(URI.create("/users/" + user.getId())).build();
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
