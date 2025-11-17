package com.cesarschool.barbearia.dominio.compartilhado.exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface ExceptionHandlerStrategy {
    public String getMessage();
    public String getName();
    public ResponseEntity<Map<String, String>> toResponseEntity();
    public HttpStatus getStatusCode();
    public Exception getOriginalException();
}
