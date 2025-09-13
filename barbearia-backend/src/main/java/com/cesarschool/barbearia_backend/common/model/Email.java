package com.cesarschool.barbearia_backend.common.model;

import java.util.regex.Pattern;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Email {
    private String value;

    public Email(String email) {
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", email)) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
        this.value = email;
    }

    @Converter(autoApply = true)
    public static class EmailConverter implements AttributeConverter<Email, String> {
        
        @Override
        public String convertToDatabaseColumn(Email email) {
            return email != null ? email.getValue() : null;
        }
        
        @Override
        public Email convertToEntityAttribute(String value) {
            return value != null ? new Email(value) : null;
        }
    }
}
