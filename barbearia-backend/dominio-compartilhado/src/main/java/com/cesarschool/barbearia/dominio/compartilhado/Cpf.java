package com.cesarschool.barbearia.dominio.compartilhado;

import java.util.Objects;

/**
 * Value Object representando um CPF válido.
 * Imutável e com validação completa.
 */
public final class Cpf {
    private final String value;

    public Cpf(String value) {
        if (!cpfValido(value)) {
            throw new IllegalArgumentException("CPF inválido: " + value);
        }
        // Armazena apenas os dígitos
        this.value = value.replaceAll("\\D", "");
    }

    public String getValue() {
        return value;
    }

    /**
     * Retorna o CPF formatado: 000.000.000-00
     */
    public String getFormatado() {
        return value.substring(0, 3) + "." +
               value.substring(3, 6) + "." +
               value.substring(6, 9) + "-" +
               value.substring(9, 11);
    }

    public static boolean cpfValido(String cpf) {
        if (cpf == null) {
            return false;
        }

        // Remove caracteres não numéricos
        String digitos = cpf.replaceAll("\\D", "");
        if (digitos.length() != 11) {
            return false;
        }

        // Rejeita CPFs com todos os dígitos iguais
        boolean todosDigitosIguais = digitos.chars().distinct().count() == 1;
        if (todosDigitosIguais) {
            return false;
        }

        // Calcula o primeiro dígito verificador (DV1)
        int somaDV1 = 0;
        for (int i = 0; i < 9; i++) {
            int num = digitos.charAt(i) - '0';
            somaDV1 += num * (10 - i);
        }
        int restoDV1 = somaDV1 % 11;
        int dv1 = (restoDV1 < 2) ? 0 : 11 - restoDV1;

        // Calcula o segundo dígito verificador (DV2)
        int somaDV2 = 0;
        for (int i = 0; i < 9; i++) {
            int num = digitos.charAt(i) - '0';
            somaDV2 += num * (11 - i);
        }
        somaDV2 += dv1 * 2;
        int restoDV2 = somaDV2 % 11;
        int dv2 = (restoDV2 < 2) ? 0 : 11 - restoDV2;

        // Verifica se os dígitos calculados coincidem com os informados
        boolean dv1Valido = dv1 == (digitos.charAt(9) - '0');
        boolean dv2Valido = dv2 == (digitos.charAt(10) - '0');

        return dv1Valido && dv2Valido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cpf)) return false;
        Cpf cpf = (Cpf) o;
        return value.equals(cpf.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return getFormatado();
    }
}
