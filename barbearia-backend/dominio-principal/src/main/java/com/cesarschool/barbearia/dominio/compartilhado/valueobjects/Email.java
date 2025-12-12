package com.cesarschool.barbearia.dominio.compartilhado.valueobjects;

import java.util.Objects;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator; 
import com.fasterxml.jackson.annotation.JsonValue;

public final class Email {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    
    private final String value;

    
    @JsonCreator 
    public Email(String value) {
        if (value == null || value.trim().isEmpty()) {   
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }
        
        String emailLimpo = value.trim().toLowerCase();
        
        if (!EMAIL_PATTERN.matcher(emailLimpo).matches()) {
            throw new IllegalArgumentException("Formato de email inválido: " + value);
        }
        
        this.value = emailLimpo;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email = (Email) o;
        return value.equals(email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}