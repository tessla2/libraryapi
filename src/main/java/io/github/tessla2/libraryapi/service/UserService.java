package io.github.tessla2.libraryapi.service;


import io.github.tessla2.libraryapi.config.MessageService;
import io.github.tessla2.libraryapi.model.User;
import io.github.tessla2.libraryapi.repository.UserRepository;
import io.github.tessla2.libraryapi.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UserValidator validator;
    private final MessageService messageService;


    @Transactional
    public void save(User user){
        if(user.getLogin() == null || user.getLogin().isBlank()) {
            throw new IllegalArgumentException(messageService.getMessage("validation.user.login.required"));
        }
        if(user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException(messageService.getMessage("validation.user.password.required"));
        }

//        validator.validateEmailDuplication(user.getEmail());
        validator.validateLoginDuplication(user.getLogin());

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

    @Transactional(readOnly = true)
    public Optional<User> getByLogin(String login) {
        return repository.findByLogin(login);
    }


    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Transactional
    public void deleteById(UUID id) {
        log.info("Deleting user: id={}", id);
        repository.deleteById(id);
    }

    @Transactional
    public User saveSocialUser(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required for social login");
        }

        User user = new User();
        user.setEmail(email);
        user.setLogin(email);
        user.setPassword(encoder.encode(UUID.randomUUID().toString()));
        user.setRoles(List.of("USER"));

        validator.validateLoginDuplication(user.getLogin());

        User saved = repository.save(user);
        log.info("Social user created: email={}, login={}", email, email);
        return saved;
    }

    public User findOrCreateByEmail(String email) {
        return repository.findByEmail(email)
                .orElseGet(() -> saveSocialUser(email));
    }
}