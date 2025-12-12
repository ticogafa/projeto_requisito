package com.cesarschool.barbearia.dominio.compartilhado.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator; 
import com.fasterxml.jackson.annotation.JsonValue;

public final class Telefone {
    private final String value;

    @JsonCreator 
    public Telefone(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Telefone não pode ser nulo");
        }
        String digitos = value.replaceAll("\\D", "");
        
        
        if (digitos.length() != 10 && digitos.length() != 11) {
            throw new IllegalArgumentException(
                "Telefone deve ter 10 (fixo) ou 11 (celular) dígitos. Recebido: " + digitos.length()
            );
        }
        
        this.value = digitos;
    }

    @JsonValue
    public String getValue() { return value; }

    /**
     * Retorna o telefone formatado.
     * Fixo: (00) 0000-0000
     * Celular: (00) 00000-0000
     */
    public String getFormatado() {
        if (value.length() == 10) {
            
            return "(" + value.substring(0, 2) + ") " +
                   value.substring(2, 6) + "-" +
                   value.substring(6, 10);
        } else {
            
            return "(" + value.substring(0, 2) + ") " +
                   value.substring(2, 7) + "-" +
                   value.substring(7, 11);
        }
    }

    public boolean isCelular() {
        return value.length() == 11;
    }

    public boolean isFixo() {
        return value.length() == 10;
    }
}