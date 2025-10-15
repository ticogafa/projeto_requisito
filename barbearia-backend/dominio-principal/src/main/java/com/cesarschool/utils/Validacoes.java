package com.cesarschool.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.commons.lang3.Validate;

/**
 * Classe utilitária para validações comuns no domínio.
 * Centraliza validações repetidas para evitar código duplicado.
 */
public final class Validacoes {
    
    private Validacoes() {
        throw new UnsupportedOperationException("Classe utilitária não deve ser instanciada");
    }

    public static void validarStringObrigatoria(String valor, String nomeCampo) {
        Validate.notBlank(valor, "%s não pode ser nulo ou vazio", nomeCampo);
    }

    public static void validarTamanhoMinimoString(String valor, int size, String nomeCampo) {
        Validacoes.validarStringObrigatoria(valor, nomeCampo);
        if (valor.length() < size){
            throw new IllegalArgumentException(
                String.format("%s deve ter pelo menos %d caracteres. Valor informado: \"%s\"", nomeCampo, size, valor)
            );
        }
    }

    public static void validarTamanhoMaximoString(String valor, int size, String nomeCampo) {
        Validate.notBlank(valor, "%s não pode ser nulo ou vazio", nomeCampo);
        if (valor.length() > size){
            throw new IllegalArgumentException(
                String.format("%s deve ter no máximo %d caracteres. Valor informado: \"%s\"", nomeCampo, size, valor)
            );
        }
    }
    
    public static void validarObjetoObrigatorio(Object valor, String nomeCampo) {
        Validate.notNull(valor, "%s não pode ser nulo", nomeCampo);
    }

    public static void validarInteiroPositivo(int valor, String nomeCampo) {
        Validate.isTrue(valor > 0, "%s deve ser positivo. Valor informado: %d", nomeCampo, valor);
    }
    
    public static void validarInteiroPositivo(Integer valor, String nomeCampo) {
        validarObjetoObrigatorio(valor, nomeCampo);
        validarInteiroPositivo(valor.intValue(), nomeCampo);
    }
    
    public static void validarInteiroNaoNegativo(int valor, String nomeCampo) {
        Validate.isTrue(valor >= 0, "%s não pode ser negativo. Valor informado: %d", nomeCampo, valor);
    }
    
    public static void validarValorPositivo(BigDecimal valor, String nomeCampo) {
        Validate.notNull(valor, "%s não pode ser nulo", nomeCampo);
        Validate.isTrue(valor.compareTo(BigDecimal.ZERO) > 0, 
            "%s deve ser positivo. Valor informado: %s", nomeCampo, valor);
    }
    
    public static void validarValorNaoNegativo(BigDecimal valor, String nomeCampo) {
        Validate.notNull(valor, "%s não pode ser nulo", nomeCampo);
        Validate.isTrue(valor.compareTo(BigDecimal.ZERO) >= 0, 
            "%s não pode ser negativo. Valor informado: %s", nomeCampo, valor);
    }
    
    public static void validarDataNaoFutura(LocalDateTime data, String nomeCampo) {
        Validate.notNull(data, "%s não pode ser nulo", nomeCampo);
        Validate.isTrue(!data.isAfter(LocalDateTime.now()), 
            "%s não pode ser no futuro. Data informada: %s", nomeCampo, data);
    }
    
    public static void validarDataNaoPassada(LocalDateTime data, String nomeCampo) {
        Validate.notNull(data, "%s não pode ser nulo", nomeCampo);
        Validate.isTrue(!data.isBefore(LocalDateTime.now()), 
            "%s não pode ser no passado. Data informada: %s", nomeCampo, data);
    }

    public static void validarInicioAntesFim(LocalDateTime inicio, LocalDateTime fim) {
        Validate.isTrue(inicio.isBefore(fim), "A data de início deve ser anterior à data de fim");
    }

    public static void validarInicioAntesFim(LocalDate inicio, LocalDate fim) {
        Validate.isTrue(inicio.isBefore(fim), "A data de início deve ser anterior à data de fim");
    }

    public static void validarInicioAntesFim(LocalTime inicio, LocalTime fim) {
        Validate.isTrue(inicio.isBefore(fim), "A hora de início deve ser anterior à hora de fim");
    }
}

