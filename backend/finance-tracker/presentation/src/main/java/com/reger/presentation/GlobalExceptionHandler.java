package com.reger.presentation;

import com.reger.application.ConflictException;
import com.reger.application.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFound(UsernameNotFoundException ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflict(ConflictException ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<String> handleAuthorizationDenied(AuthorizationDeniedException ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}