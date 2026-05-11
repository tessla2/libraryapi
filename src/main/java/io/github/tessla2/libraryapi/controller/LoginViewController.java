package io.github.tessla2.libraryapi.controller;


import io.github.tessla2.libraryapi.security.CustomAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class LoginViewController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/")
    @ResponseBody
    public String homePage(Authentication authentication) {
        if(authentication instanceof CustomAuthentication customAuth) {
            log.info("Authenticated user: {}", customAuth.getUser().getLogin());
        }
        return "Hello, welcome to the library!";
    }
}
