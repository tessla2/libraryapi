package io.github.tessla2.libraryapi.service;


import io.github.tessla2.libraryapi.client.GoogleBooksClient;
import io.github.tessla2.libraryapi.controller.dto.AuthorDTO;
import io.github.tessla2.libraryapi.controller.dto.BookRegistrationDTO;
import io.github.tessla2.libraryapi.controller.dto.SearchBookDTO;
import io.github.tessla2.libraryapi.controller.dto.google.BookImportResult;
import io.github.tessla2.libraryapi.controller.dto.google.GoogleBookResponse;
import io.github.tessla2.libraryapi.exceptions.GoogleBooksApiException;
import io.github.tessla2.libraryapi.exceptions.OperationNotAllowed;
import io.github.tessla2.libraryapi.mappers.BookMapper;
import io.github.tessla2.libraryapi.mappers.GoogleBookMapper;
import io.github.tessla2.libraryapi.model.Author;
import io.github.tessla2.libraryapi.model.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleBooksService {

    private final GoogleBooksClient client;
    private final GoogleBookMapper googleBookMapper;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BookMapper bookMapper;

    public GoogleBookResponse search(String query, int maxResults, int startIndex) {
        log.info("Searching Google Books: query='{}', maxResults={}, startIndex={}", query, maxResults, startIndex);
        return client.searchBooks(query, maxResults, startIndex);
    }

    public Optional<GoogleBookResponse.Volume> getByVolumeId(String volumeId) {
        try {
            return Optional.ofNullable(client.getBookByVolumeId(volumeId));
        } catch (GoogleBooksApiException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                return Optional.empty();
            }
            throw e;
        }
    }

    public Optional<GoogleBookResponse.Volume> searchByIsbn(String isbn) {
        try {
            var response = client.searchByIsbn(isbn);
            if (response != null && response.items() != null && !response.items().isEmpty()) {
                return Optional.of(response.items().getFirst());
            }
        } catch (GoogleBooksApiException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                return Optional.empty();
            }
            throw e;
        }
        return Optional.empty();
    }

    // Importa automaticamente um livro da Google Books para a base local
    @Transactional
    public BookImportResult importByIsbn(String isbn) {
        // 1. Buscar o livro na Google Books pelo ISBN
        var volume = searchByIsbn(isbn)
                .orElseThrow(() -> new OperationNotAllowed(
                        "Book not found on Google Books for ISBN: " + isbn));

        var info = volume.volumeInfo();

        // 2. Mapear os dados do volume para um BookRegistrationDTO (sem idAuthor)
        var partialDto = googleBookMapper.toBookRegistrationDTO(volume);

        // 3. Extrair nome do autor do volumeInfo.authors[]
        String authorName = (info.authors() != null && info.authors().length > 0)
                ? info.authors()[0] : "Unknown Author";
        String finalAuthorName = authorName; // variavel efetivamente final para lambda

        // 4. Procurar autor na base local pelo nome exato, ou criar um novo
        var existingAuthor = authorService.findByNameExact(finalAuthorName);
        boolean authorCreated = existingAuthor.isEmpty();
        Author author = existingAuthor.orElseGet(() -> authorService.createMinimal(finalAuthorName));

        // 5. Montar o DTO completo com o id do autor e salvar o livro
        var fullDto = new BookRegistrationDTO(
                partialDto.isbn(),
                partialDto.title(),
                partialDto.publicationDate(),
                partialDto.genre(),
                partialDto.price(),
                author.getId()
        );

        Book book = bookMapper.toEntity(fullDto);
        Book saved = bookService.save(book);

        log.info("Book imported from Google Books: title='{}', isbn={}, author='{}'",
                saved.getTitle(), saved.getIsbn(), author.getName());

        // 6. Montar resposta com dados do livro importado
        var authorDTO = new AuthorDTO(
                author.getId(), author.getName(),
                author.getNationality(), author.getBirthDate());

        return new BookImportResult(
                saved.getId(), saved.getIsbn(), saved.getTitle(),
                saved.getPublicationDate(), saved.getGenre(),
                saved.getPrice(), authorDTO, authorCreated);
    }
}
