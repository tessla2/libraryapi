package io.github.tessla2.libraryapi.controller.common;


import io.github.tessla2.libraryapi.controller.dto.ErrorResponse;
import io.github.tessla2.libraryapi.controller.dto.InvalidField;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
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

}
