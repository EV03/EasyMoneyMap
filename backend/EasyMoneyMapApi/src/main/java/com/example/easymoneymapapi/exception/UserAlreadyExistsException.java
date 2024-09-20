package com.example.easymoneymapapi.exception;
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException( String message ) {
        super(message);
    }
}
