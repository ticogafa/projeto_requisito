package com.cesarschool.barbearia.dominio.compartilhado.exceptions;

import java.util.function.Supplier;

import org.springframework.http.ResponseEntity;

public class ExceptionHandler {
    
    public <R> ResponseEntity<Object> withHandler(Supplier<ResponseEntity<Object>> callable) {
        try{
            return callable.get();
        } catch (Exception e){
            var handler = getHandler(e);
            return handler;
        }
    }

    public ResponseEntity<Object> getHandler(Exception e){
        return ResponseEntity
        .badRequest()
        .body(e.getMessage());
    }

    
}
