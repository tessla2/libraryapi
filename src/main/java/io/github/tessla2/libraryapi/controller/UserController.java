package io.github.tessla2.libraryapi.controller;


import io.github.tessla2.libraryapi.controller.dto.UserDTO;
import io.github.tessla2.libraryapi.mappers.UserMapper;
import io.github.tessla2.libraryapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody UserDTO userDTO) {
        var user = mapper.toEntity(userDTO);
        service.save(user);
    }

}
