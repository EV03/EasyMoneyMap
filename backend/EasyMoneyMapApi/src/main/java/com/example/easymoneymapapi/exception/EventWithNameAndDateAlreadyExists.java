package com.example.easymoneymapapi.exception;

public class EventWithNameAndDateAlreadyExists extends RuntimeException {
    public EventWithNameAndDateAlreadyExists(String message) {
        super(message);
    }
}
