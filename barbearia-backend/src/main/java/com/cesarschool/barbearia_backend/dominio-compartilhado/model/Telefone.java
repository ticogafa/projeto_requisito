package com.cesarschool.barbearia_backend.common.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Telefone {
    private String value;
    
    public Telefone(String value) {
        if (value.length() != 10) {
            throw new IllegalArgumentException("Número de telefone deve ter 10 dígitos");
        }
        this.value = value;
    }
}
