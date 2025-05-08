package com.lewyonq.so_cards_app.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String className, Long id) {
        super(className + " with id " + id + " not found");
    }
}
