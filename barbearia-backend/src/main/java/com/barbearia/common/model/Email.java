package com.barbearia.common.model;

import java.util.regex.Pattern;

import lombok.Data;

@Data
public class Email {
    private String value;

    public Email(String email) {
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", email)) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
        this.value = email;
    }
}
