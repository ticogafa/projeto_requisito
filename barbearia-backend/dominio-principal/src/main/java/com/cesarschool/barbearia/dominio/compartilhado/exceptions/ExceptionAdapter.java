package com.cesarschool.barbearia.dominio.compartilhado.exceptions;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;

@Getter
public class ExceptionAdapter {
    private final HttpStatus statusCode;
    private final Exception originalException;

    public ExceptionAdapter(Exception origException, HttpStatus statusCode){
        this.originalException=origException;
        this.statusCode=statusCode;
    }

    public ExceptionAdapter(Exception origException){
        this.originalException=origException;
        this.statusCode=HttpStatus.BAD_REQUEST;
    }

    public String getMessage(){
        return originalException.getMessage();
    }

    public String getName(){
        return originalException.getClass().getName();
    }

    public ResponseEntity<Map<String, String>> toResponseEntity(){
        Map<String, String> body = new HashMap<>();
        String timestamp = ZonedDateTime
            .now()
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        
        body.put("name", getName());
        body.put("message", getMessage());
        body.put("statusCode", getStatusCode().toString());
        body.put("timestamp", timestamp);

        return ResponseEntity
        .status(getStatusCode())
        .body(body);
    }
}
