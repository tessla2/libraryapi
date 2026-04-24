package io.github.tessla2.libraryapi.service;


import io.github.tessla2.libraryapi.model.User;
import io.github.tessla2.libraryapi.repository.UserRepository;
import io.github.tessla2.libraryapi.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UserValidator validator;


    @Transactional
    public void save(User user){
        if(user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if(user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        var password = user.getPassword();
        user.setPassword(encoder.encode(password));
        repository.save(user);
    }

    @Transactional
    public void delete(User user){
      validator.validate(user);
      repository.delete(user);
    }

    @Transactional
    public void update(User user) {
        validator.validate(user);
        repository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<User> search(String email, Pageable pageable) {
        if(email == null || email.isBlank()) {
            return repository.findAll(pageable);
        }
        return repository.findByEmailContaining(email, pageable);
    }

}