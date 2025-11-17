package com.cesarschool.barbearia.dominio.compartilhado.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ExceptionEntry {
    private final Class<? extends GenericExceptionHandlerStrategy> adapterClass;
    private final HttpStatus status;

    public ExceptionEntry(Class<? extends GenericExceptionHandlerStrategy> adapterClass, HttpStatus status) {
        this.adapterClass = adapterClass;
        this.status = status;
    }
}
