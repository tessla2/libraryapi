package io.github.tessla2.libraryapi.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class GoogleBooksConfig {

    @Bean
    public RestTemplate googleBooksRestTemplate(RestTemplateBuilder builder) {
        return builder
                .rootUri("https://www.googleapis.com/books/v1")
                .defaultHeader("User-Agent", "LibraryAPI/1.0")
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
    }
}
