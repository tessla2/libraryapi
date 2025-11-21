package io.github.tessla2.libraryapi.common;


import io.github.tessla2.libraryapi.controller.dto.ErrorResponse;
import io.github.tessla2.libraryapi.controller.dto.InvalidField;
import io.github.tessla2.libraryapi.exceptions.DuplicateRecordException;
import io.github.tessla2.libraryapi.exceptions.InvalidComponentException;
import io.github.tessla2.libraryapi.exceptions.OperationNotAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<InvalidField> errorsList = fieldErrors
                .stream()
                .map(fe -> new InvalidField(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation Error. ",
                errorsList);

    }

    @ExceptionHandler(DuplicateRecordException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateRecordException(DuplicateRecordException e) {
        return ErrorResponse.conflictError(e.getMessage());

    }

    @ExceptionHandler(OperationNotAllowed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleOperationNotAllowed(OperationNotAllowed e) {
        return ErrorResponse.standardError(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleInvalidComponentException(InvalidComponentException e){
        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Error in component validation.",
                List.of(new InvalidField(e.getField(), e.getMessage()))
                );

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpected(Exception e) {
        log.error("Unexpected error: ", e);

        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error has occurred",
                List.of()
        );
    }
}
