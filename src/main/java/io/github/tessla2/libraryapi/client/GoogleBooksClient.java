package io.github.tessla2.libraryapi.client;

import io.github.tessla2.libraryapi.controller.dto.google.GoogleBookResponse;
import io.github.tessla2.libraryapi.exceptions.GoogleBooksApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class GoogleBooksClient {

    private final RestTemplate googleBooksRestTemplate;
    private final String apiKey;

    public GoogleBooksClient(RestTemplate googleBooksRestTemplate,
                             @Value("${google.books.api-key}") String apiKey) {
        this.googleBooksRestTemplate = googleBooksRestTemplate;
        this.apiKey = apiKey;
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("GOOGLE_BOOKS_API_KEY is null or empty - Google Books API calls will fail");
        } else {
            log.info("Google Books API key loaded: {}", maskKey(apiKey));
        }
    }

    public GoogleBookResponse searchBooks(String query, int maxResults, int startIndex) {
        log.info("Calling Google Books API: query='{}'", query);
        var url = "/volumes?q={query}&maxResults={maxResults}&startIndex={startIndex}&key={key}";
        return callApi(() -> googleBooksRestTemplate.getForObject(
                url, GoogleBookResponse.class, query, maxResults, startIndex, apiKey));
    }

    public GoogleBookResponse.Volume getBookByVolumeId(String volumeId) {
        var url = "/volumes/{volumeId}?key={key}";
        return callApi(() -> googleBooksRestTemplate.getForObject(
                url, GoogleBookResponse.Volume.class, volumeId, apiKey));
    }

    public GoogleBookResponse searchByIsbn(String isbn) {
        return searchBooks("isbn:" + isbn, 1, 0);
    }

    private <T> T callApi(ApiCall<T> call) {
        try {
            return call.execute();
        } catch (HttpClientErrorException e) {
            log.error("Google Books API {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new GoogleBooksApiException(
                    e.getStatusCode().value(),
                    e.getResponseBodyAsString(),
                    "Google Books API error: " + e.getStatusCode().value());
        } catch (HttpServerErrorException e) {
            log.error("Google Books API {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new GoogleBooksApiException(
                    e.getStatusCode().value(),
                    e.getResponseBodyAsString(),
                    "Google Books API temporarily unavailable");
        } catch (RestClientException e) {
            log.error("Google Books connection error: {}", e.getMessage());
            throw new GoogleBooksApiException(
                    HttpStatus.SERVICE_UNAVAILABLE.value(),
                    null,
                    "Failed to connect to Google Books API: " + e.getMessage());
        }
    }

    private String maskKey(String key) {
        if (key == null || key.length() < 8) return "***";
        return key.substring(0, 4) + "..." + key.substring(key.length() - 4);
    }

    @FunctionalInterface
    private interface ApiCall<T> {
        T execute();
    }
}
