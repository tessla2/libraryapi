package io.github.tessla2.libraryapi.controller.common;


import io.github.tessla2.libraryapi.controller.dto.ErrorResponse;
import io.github.tessla2.libraryapi.controller.dto.InvalidField;
import io.github.tessla2.libraryapi.exceptions.DuplicateRecordException;
import io.github.tessla2.libraryapi.exceptions.OperationNotAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
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
    public ErrorResponse handleDuplicateRecordException(DuplicateRecordException e){
        return ErrorResponse.conflictError(e.getMessage());

        }

    @ExceptionHandler(OperationNotAllowed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleOperationNotAllowed(OperationNotAllowed e){
        return ErrorResponse.standardError(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUntreatedErrors(RuntimeException e){
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error has occurred",
                List.of());
    }
}
