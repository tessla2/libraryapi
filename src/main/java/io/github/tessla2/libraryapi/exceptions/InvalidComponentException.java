package io.github.tessla2.libraryapi.exceptions;

import lombok.Getter;

public class InvalidComponentException extends RuntimeException {

    @Getter
    private String field;


    public InvalidComponentException(String field, String message) {
        super(message);
        this.field = field;
    }








}
