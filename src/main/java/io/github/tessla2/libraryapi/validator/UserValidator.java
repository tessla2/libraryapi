package io.github.tessla2.libraryapi.validator;

import io.github.tessla2.libraryapi.model.User;
import io.github.tessla2.libraryapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import io.github.tessla2.libraryapi.exceptions.OperationNotAllowed;


@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository repository;

    public void validate(User user){
        if(user == null || user.getId() == null) {
            throw new OperationNotAllowed("User doesn't exist");
        }
        if(!repository.existsById(user.getId())) {
            throw new OperationNotAllowed("User doesn't exist");
        }
    }
}