package io.github.tessla2.libraryapi.security;

import io.github.tessla2.libraryapi.model.User;
import io.github.tessla2.libraryapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService service;

    @Override
    public UserDetails loadUserByUsername(String login) {

        User user = service.getByLogin(login)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with login: " + login));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0]))
                .build();
    }
}
