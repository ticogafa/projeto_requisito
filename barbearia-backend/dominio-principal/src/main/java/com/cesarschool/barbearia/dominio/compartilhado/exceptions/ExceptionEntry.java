package com.cesarschool.barbearia.dominio.compartilhado.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ExceptionEntry {
    private final Class<? extends ExceptionHandlerStrategy> strategyClass;
    private final HttpStatus status;

    public ExceptionEntry(Class<? extends ExceptionHandlerStrategy> strategyClass, HttpStatus status) {
        this.strategyClass = strategyClass;
        this.status = status;
    }
}
