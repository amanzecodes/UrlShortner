package com.urlshortner.UrlShortner.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("An account with that email already exists: " + email);
    }
}