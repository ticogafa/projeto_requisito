package com.cesarschool.barbearia_backend.common.model;

import java.util.regex.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Email {
    private String value;

    public Email(String email) {
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", email)) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
        this.value = email;
    }
}

