package com.example.vivacventures.domain.modelo.exceptions;

public class BadPasswordException extends RuntimeException {

    public BadPasswordException(String message) {
        super(message);
    }

}
