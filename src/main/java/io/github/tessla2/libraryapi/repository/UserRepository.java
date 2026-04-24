package io.github.tessla2.libraryapi.repository;

import io.github.tessla2.libraryapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    List<User> findByEmailContaining(String email);
    Page<User> findByEmailContaining(String email, Pageable pageable);
}