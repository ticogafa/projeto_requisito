package com.cesarschool.barbearia_backend.common.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Telefone {
    private String value;
    
    public Telefone(String value) {
        if (value.length() != 10) {
            throw new IllegalArgumentException("Número de telefone inválido");
        }
        this.value = value;
    }

    @Converter(autoApply = true)
    public static class TelefoneConverter implements AttributeConverter<Telefone, String> {
        
        @Override
        public String convertToDatabaseColumn(Telefone telefone) {
            return telefone != null ? telefone.getValue() : null;
        }
        
        @Override
        public Telefone convertToEntityAttribute(String value) {
            return value != null ? new Telefone(value) : null;
        }
    }
}
