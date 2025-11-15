package com.cesarschool.barbearia.dominio.compartilhado.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ExceptionEntry {
    private final Class<? extends ExceptionAdapter> adapterClass;
    private final HttpStatus status;

    public ExceptionEntry(Class<? extends ExceptionAdapter> adapterClass, HttpStatus status) {
        this.adapterClass = adapterClass;
        this.status = status;
    }
}
