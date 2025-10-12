package com.cesarschool.barbearia_backend.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Cpf {
    private String value;

    public Cpf(String value) {
        if (!cpfValido(value)) {
            throw new IllegalArgumentException("CPF inválido");
        }
        this.value = value;
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

}
