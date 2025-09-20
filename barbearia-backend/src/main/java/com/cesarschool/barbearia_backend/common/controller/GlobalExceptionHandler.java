package com.cesarschool.barbearia_backend.common.controller;


import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cesarschool.barbearia_backend.common.exceptions.DuplicateException;
import com.cesarschool.barbearia_backend.common.exceptions.NotFoundException;

import jakarta.persistence.EntityNotFoundException;

/*
 * Essa classe é responsáveis por tratar as exceções lançadas pela aplicação e traduzir nos codigos de status HTTP corretos
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    public record ErrorResponse(
        HttpStatus status,
        String errorCode,
        String message,
        LocalDateTime timestamp
    ) {
        public ErrorResponse(HttpStatus status, String errorCode, String message) {
            this(status, errorCode, message, LocalDateTime.now());
        }
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> handleConflictExceptions(RuntimeException ex) {
        return this.buildResponse(
            HttpStatus.CONFLICT,
            ex.getClass().getSimpleName(),
            ex.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(RuntimeException ex) {
        return this.buildResponse(
            HttpStatus.BAD_REQUEST,
            "invalid_request",
            ex.getMessage()
        );
    }

    @ExceptionHandler({
        EntityNotFoundException.class,
        NotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(RuntimeException ex) {
        return this.buildResponse(
            HttpStatus.NOT_FOUND,
            "resource_not_found",
            ex.getMessage()
        );
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining("; "));

        return this.buildResponse(
            HttpStatus.BAD_REQUEST,
            "validation_error",
            errorMessage
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException ex) {
        return this.buildResponse(
            HttpStatus.CONFLICT,
            "data_integrity_violation",
            ex.getMostSpecificCause().getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return this.buildResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "internal_error",
            "Exceção não tradata: " +ex+" "+ex.getMessage()
        );
    }
        
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String errorCode, String message) {
        return ResponseEntity
        .status(status)
        .body(new ErrorResponse(status, errorCode, message));
    }
    }
