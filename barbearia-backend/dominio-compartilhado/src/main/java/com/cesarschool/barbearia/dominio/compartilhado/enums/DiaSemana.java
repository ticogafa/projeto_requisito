package com.cesarschool.barbearia.dominio.compartilhado.enums;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * Enum representando os dias da semana.
 * Adaptado do código original para Clean Architecture.
 */
public enum DiaSemana {
    SEGUNDA("Segunda-feira", DayOfWeek.MONDAY),
    TERCA("Terça-feira", DayOfWeek.TUESDAY),
    QUARTA("Quarta-feira", DayOfWeek.WEDNESDAY),
    QUINTA("Quinta-feira", DayOfWeek.THURSDAY),
    SEXTA("Sexta-feira", DayOfWeek.FRIDAY),
    SABADO("Sábado", DayOfWeek.SATURDAY),
    DOMINGO("Domingo", DayOfWeek.SUNDAY);

    private final String nomeCompleto;
    private final DayOfWeek dayOfWeek;

    DiaSemana(String nomeCompleto, DayOfWeek dayOfWeek) {
        this.nomeCompleto = nomeCompleto;
        this.dayOfWeek = dayOfWeek;
    }

    public String getNome() {
        return nomeCompleto;
    }

    public DayOfWeek toDayOfWeek() {
        return dayOfWeek;
    }

    public static DiaSemana fromLocalDateTime(LocalDateTime dateTime) {
        return fromDayOfWeek(dateTime.getDayOfWeek());
    }

    public static DiaSemana fromDayOfWeek(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return SEGUNDA;
            case TUESDAY: return TERCA;
            case WEDNESDAY: return QUARTA;
            case THURSDAY: return QUINTA;
            case FRIDAY: return SEXTA;
            case SATURDAY: return SABADO;
            case SUNDAY: return DOMINGO;
            default: throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);
        }
    }
}
