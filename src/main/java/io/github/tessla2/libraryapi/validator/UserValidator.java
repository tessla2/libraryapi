package io.github.tessla2.libraryapi.validator;

import io.github.tessla2.libraryapi.config.MessageService;
import io.github.tessla2.libraryapi.exceptions.DuplicateRecordException;
import io.github.tessla2.libraryapi.model.User;
import io.github.tessla2.libraryapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import io.github.tessla2.libraryapi.exceptions.OperationNotAllowed;


@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository repository;
    private final MessageService messageService;

    public void validate(User user){
        if(user == null || user.getId() == null) {
            throw new OperationNotAllowed(messageService.getMessage("validation.user.not-found"));
        }
        if(!repository.existsById(user.getId())) {
            throw new OperationNotAllowed(messageService.getMessage("validation.user.not-found"));
        }
    }

//    public void validateEmailDuplication(String email) {
//        if (email != null && repository.findByEmail(email).isPresent()) {
//            throw new DuplicateRecordException(messageService.getMessage("validation.user.email.duplicate"));
//        }
//    }

    public void validateLoginDuplication(String login) {
        if (login != null && repository.findByLogin(login).isPresent()) {
            throw new DuplicateRecordException(messageService.getMessage("validation.user.login.duplicate"));
        }
    }
}