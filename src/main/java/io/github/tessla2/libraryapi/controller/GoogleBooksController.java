package io.github.tessla2.libraryapi.controller;

import io.github.tessla2.libraryapi.controller.dto.BookRegistrationDTO;
import io.github.tessla2.libraryapi.controller.dto.google.BookImportResult;
import io.github.tessla2.libraryapi.controller.dto.google.GoogleBookResponse;
import io.github.tessla2.libraryapi.mappers.GoogleBookMapper;
import io.github.tessla2.libraryapi.service.GoogleBooksService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/google-books")
@RequiredArgsConstructor
public class GoogleBooksController {

    private final GoogleBooksService googleBooksService;
    private final GoogleBookMapper googleBookMapper;

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<GoogleBookResponse> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") @Min(1) @Max(40) int maxResults,
            @RequestParam(defaultValue = "0") @Min(0) int startIndex) {
        var response = googleBooksService.search(query, maxResults, startIndex);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{volumeId}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<GoogleBookResponse.Volume> getVolume(
            @PathVariable String volumeId) {
        return googleBooksService.getByVolumeId(volumeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/isbn/{isbn}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<GoogleBookResponse.Volume> getByIsbn(@PathVariable String isbn) {
        return googleBooksService.searchByIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/isbn/{isbn}/auto-fill")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<BookRegistrationDTO> autoFillByIsbn(@PathVariable String isbn) {
        return googleBooksService.searchByIsbn(isbn)
                .map(googleBookMapper::toBookRegistrationDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Importa automaticamente: busca livro no Google, cria autor se necessario,
    // salva na base local e retorna os dados completos
    @PostMapping("/isbn/{isbn}/import")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<BookImportResult> importByIsbn(@PathVariable String isbn) {
        var result = googleBooksService.importByIsbn(isbn);
        return ResponseEntity.ok(result);
    }
}
