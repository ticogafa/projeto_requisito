package com.cesarschool.barbearia.dominio.compartilhado.exceptions;

import java.util.function.Supplier;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ExceptionHandler {
    
    private final ExceptionRegistry registry;

    public ExceptionHandler(ExceptionRegistry registry) {
        this.registry = registry;
    }

    @PostConstruct
    public void init() {
        registry.configureDefaults();
    }

    /**
     * Executa uma operação envolvida em tratamento de exceções.
     * @param callable Operação a ser executada
     * @return ResponseEntity com o resultado ou erro tratado
     */
    @SuppressWarnings("unchecked")
    public <T> ResponseEntity<T> withHandler(Supplier<ResponseEntity<T>> callable) {
        try {
            return callable.get();
        } catch (Exception e) {
            ExceptionAdapter adapter = registry.getAdapter(e);
            return (ResponseEntity<T>)(Object) adapter.toResponseEntity();
        }
    }
}
