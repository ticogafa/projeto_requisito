package com.cesarschool.barbearia_backend.common.enums;

public enum DiaSemana {
    SEGUNDA("Segunda-feira"),
    TERCA("Terça-feira"),
    QUARTA("Quarta-feira"),
    QUINTA("Quinta-feira"),
    SEXTA("Sexta-feira"),
    SABADO("Sábado"),
    DOMINGO("Domingo");

    private final String nomeCompleto;

    DiaSemana(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getNome() {
        return nomeCompleto;
    }

    public static DiaSemana fromLocalDateTime(java.time.LocalDateTime dateTime) {
        switch (dateTime.getDayOfWeek()) {
            case MONDAY: return SEGUNDA;
            case TUESDAY: return TERCA;
            case WEDNESDAY: return QUARTA;
            case THURSDAY: return QUINTA;
            case FRIDAY: return SEXTA;
            case SATURDAY: return SABADO;
            case SUNDAY: return DOMINGO;
            default: throw new IllegalArgumentException("Invalid day of week");
        }
    }
}
