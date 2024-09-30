package com.example.easymoneymapapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler ist verantwortlich für die zentrale Behandlung von Ausnahmen,
 * die in der gesamten Anwendung auftreten.
 *
 * Diese Klasse stellt sicher, dass alle Validierungsfehler und andere Ausnahmen
 * einheitlich behandelt und geeignete Fehlermeldungen an den Client zurückgegeben werden.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    /** Behandelt Validierungsfehler, die während der Verarbeitung von Registrierungsanfragen auftreten.
     * (starter validation hat nicht funktioniert)
     * @param ex @param ex die ausgelöste ValidaitonException
     * @return eine ResponseEntity mit HTTP-Status '400-BAD REQUEST'
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("errors", ex.getErrors());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    /**
     * Behandelt die Ausnahme, wenn ein Benutzer bereits existiert.
     *
     * @param ex die ausgelöste UserAlreadyExistsException, die den Konflikt beschreibt
     * @return ResponseEntity mit einer Fehlermeldung im JSON-Format und dem HTTP-Status '409 CONFLICT'
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistException(UserAlreadyExistsException ex ) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    /**
     * Behandelt die Ausnahme, wenn beim Loginversuch Username und Passwort nicht übereinstimmen
     * @param ex die ausgelöste BadCredentialsException
     * @return ResponseEntity mit einer Fehlermeldung im JSON-Format und dem HTTP-Status '401 UNAUTHORIZED'
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentialsException(BadCredentialsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    /**
     * Behandelt die Ausnahme, wenn ein Benutzer nicht gefunden wird
     * @param ex die ausgelöste UsernameNotFoundException
     * @return ResponseEntity mit einer Fehlermeldung im JSON-Format und dem HTTP-Status '404 NOT FOUND'
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Behandelt allgemeine Ausnahmen, die nicht speziell behandelt werden.
     *
     * @param ex die ausgelöste Exception
     * @return eine ResponseEntity mit einer allgemeinen Fehlermeldung im JSON-Format
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Server Fehler");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /** Behandelt die Ausnahme, wenn ein Event nicht gefunden wird
     * @param ex die ausgelöste EventNotFoundException
     * @return ResponseEntity mit einer Fehlermeldung im JSON-Format und dem HTTP-Status '404 NOT FOUND'
     */
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEventNotFoundException(EventNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Behandlet die Ausnahme, wenn ein Event mit gleichen Namen und Datum bereits existiert
     * @param ex die ausgelöste EventAlreadyExistsException
     * @return ResponseEntity mit einer Fehlermeldung im JSON-Format und dem HTTP-Status '409 CONFLICT'
     */

    @ExceptionHandler(EventWithNameAndDateAlreadyExists.class)
    public ResponseEntity<Map<String, String>> handleEventAlreadyExistsException(EventWithNameAndDateAlreadyExists ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * behandelt die Ausnahme, wenn dateTo vor dateFrom liegt
     * @param ex die ausgelöste IllegalArgumentException
     *
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * behandelt die aussnahme, wenn ein User in einem Event nicht gefunden wird
     * @param ex die ausgelöste UserInEventNotFoundException
     *
     */
    @ExceptionHandler(UserInEventNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserInEventNotFoundException(UserInEventNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * behandelt die Ausnahme, wenn ein unautorisierter Zugriff erfolgt
     * @param ex die ausgelöste UnauthorizedAccessException
     * @return ResponseEntity mit einer Fehlermeldung im JSON-Format und dem HTTP-Status '401 UNAUTHORIZED'
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

}
