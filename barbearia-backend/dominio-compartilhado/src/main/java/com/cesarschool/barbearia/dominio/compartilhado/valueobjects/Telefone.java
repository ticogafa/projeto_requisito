package com.cesarschool.barbearia.dominio.compartilhado.valueobjects;

/**
 * Value Object representando um Telefone válido.
 * Imutável e com validação de formato.
 * Suporta telefones fixos (10 dígitos) e celulares (11 dígitos).
 */
public final class Telefone {
    private final String value;

    public Telefone(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Telefone não pode ser nulo");
        }
        
        // Remove caracteres não numéricos
        String digitos = value.replaceAll("\\D", "");
        
        // Valida: deve ter 10 (fixo) ou 11 (celular) dígitos
        if (digitos.length() != 10 && digitos.length() != 11) {
            throw new IllegalArgumentException(
                "Telefone deve ter 10 (fixo) ou 11 (celular) dígitos. Recebido: " + digitos.length()
            );
        }
        
        this.value = digitos;
    }

    public String getValue() { return value; }

    /**
     * Retorna o telefone formatado.
     * Fixo: (00) 0000-0000
     * Celular: (00) 00000-0000
     */
    public String getFormatado() {
        if (value.length() == 10) {
            // Fixo: (00) 0000-0000
            return "(" + value.substring(0, 2) + ") " +
                   value.substring(2, 6) + "-" +
                   value.substring(6, 10);
        } else {
            // Celular: (00) 00000-0000
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
