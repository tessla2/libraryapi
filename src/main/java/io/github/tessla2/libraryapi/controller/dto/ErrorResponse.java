package io.github.tessla2.libraryapi.controller.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponse(int status, String message, List<InvalidField> error) {

    public static ErrorResponse standardError(String message) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }

    public static ErrorResponse conflictError(String message) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), message, List.of());
    }


}
