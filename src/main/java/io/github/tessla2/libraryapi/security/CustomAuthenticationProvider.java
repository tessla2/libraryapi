package io.github.tessla2.libraryapi.security;


import io.github.tessla2.libraryapi.model.User;
import io.github.tessla2.libraryapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<User> byLogin = userService.getByLogin(login);

        if(byLogin.isEmpty()){
            throw getErrorUserNotFoundException();
        }

        String encodedPassword = byLogin.get().getPassword();

        if (encodedPassword == null) {
            return null;
        }

        boolean matches = encoder.matches(password, encodedPassword);
        if(matches){
            return new CustomAuthentication(byLogin.get());
        }

        return null;
    }

    private static UsernameNotFoundException getErrorUserNotFoundException() {
        return new UsernameNotFoundException("User or password incorrect");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
