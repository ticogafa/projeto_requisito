package com.cesarschool.barbearia.dominio.compartilhado.utils;

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
    
    /**
     * Valida que uma string não é nula e não está em branco.
     */
    public static void validarStringObrigatoria(String valor, String nomeCampo) {
        Validate.notBlank(valor, "%s não pode ser nulo ou vazio", nomeCampo);
    }
    /**
     * Valida que uma string não é nula e não está em branco.
     */
    public static void validarTamanhoMinimoString(String valor, int size, String nomeCampo) {
        Validacoes.validarStringObrigatoria(valor, nomeCampo);
        if (valor.length() < size){
            throw new IllegalArgumentException(
                String.format("%s deve ter pelo menos %d caracteres. Valor informado: \"%s\"", nomeCampo, size, valor)
            );
        }
    }
    /**
     * Valida que uma string não é nula e não está em branco.
     */
    public static void validarTamanhoMaximoString(String valor, int size, String nomeCampo) {
        Validate.notBlank(valor, "%s não pode ser nulo ou vazio", nomeCampo);
        if (valor.length() > size){
            throw new IllegalArgumentException(
                String.format("%s deve ter no máximo %d caracteres. Valor informado: \"%s\"", nomeCampo, size, valor)
            );
        }
    }
    
    /**
     * Valida que um objeto não é nulo.
     */
    public static void validarObjetoObrigatorio(Object valor, String nomeCampo) {
        Validate.notNull(valor, "%s não pode ser nulo", nomeCampo);
    }
    
    /**
     * Valida que um número inteiro é positivo.
     */
    public static void validarInteiroPositivo(int valor, String nomeCampo) {
        Validate.isTrue(valor > 0, "%s deve ser positivo. Valor informado: %d", nomeCampo, valor);
    }
    
    /**
     * Valida que um número inteiro (Integer) não é nulo e é positivo.
     */
    public static void validarInteiroPositivo(Integer valor, String nomeCampo) {
        validarObjetoObrigatorio(valor, nomeCampo);
        validarInteiroPositivo(valor.intValue(), nomeCampo);
    }
    
    /**
     * Valida que um número inteiro não é negativo.
     */
    public static void validarInteiroNaoNegativo(int valor, String nomeCampo) {
        Validate.isTrue(valor >= 0, "%s não pode ser negativo. Valor informado: %d", nomeCampo, valor);
    }
    
    /**
     * Valida que um BigDecimal é positivo.
     */
    public static void validarValorPositivo(BigDecimal valor, String nomeCampo) {
        Validate.notNull(valor, "%s não pode ser nulo", nomeCampo);
        Validate.isTrue(valor.compareTo(BigDecimal.ZERO) > 0, 
            "%s deve ser positivo. Valor informado: %s", nomeCampo, valor);
    }
    
    /**
     * Valida que um BigDecimal não é negativo.
     */
    public static void validarValorNaoNegativo(BigDecimal valor, String nomeCampo) {
        Validate.notNull(valor, "%s não pode ser nulo", nomeCampo);
        Validate.isTrue(valor.compareTo(BigDecimal.ZERO) >= 0, 
            "%s não pode ser negativo. Valor informado: %s", nomeCampo, valor);
    }
    
    /**
     * Valida que uma data não é futura.
     */
    public static void validarDataNaoFutura(LocalDateTime data, String nomeCampo) {
        Validate.notNull(data, "%s não pode ser nulo", nomeCampo);
        Validate.isTrue(!data.isAfter(LocalDateTime.now()), 
            "%s não pode ser no futuro. Data informada: %s", nomeCampo, data);
    }
    
    /**
     * Valida que uma data não é passada.
     */
    public static void validarDataNaoPassada(LocalDateTime data, String nomeCampo) {
        Validate.notNull(data, "%s não pode ser nulo", nomeCampo);
        Validate.isTrue(!data.isBefore(LocalDateTime.now()), 
            "%s não pode ser no passado. Data informada: %s", nomeCampo, data);
    }
    /**
     * Valida que uma data não é passada.
     */
    public static void validarInicioAntesFim(LocalDateTime inicio, LocalDateTime fim) {
        Validate.isTrue(inicio.isBefore(fim), "A data de início deve ser anterior à data de fim");
    }
    /**
     * Valida que uma data não é passada.
     */
    public static void validarInicioAntesFim(LocalDate inicio, LocalDate fim) {
        Validate.isTrue(inicio.isBefore(fim), "A data de início deve ser anterior à data de fim");
    }

    /**
     * Valida que uma data não é passada.
     */
    public static void validarInicioAntesFim(LocalTime inicio, LocalTime fim) {
        Validate.isTrue(inicio.isBefore(fim), "A hora de início deve ser anterior à hora de fim");
    }
}

