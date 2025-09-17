package com.cesarschool.barbearia_backend.profissionais.dto;

import java.time.LocalTime;
import java.util.List;

import com.cesarschool.barbearia_backend.common.enums.DiaSemana;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

public final class HorarioTrabalhoDTOs {

    private HorarioTrabalhoDTOs() {
        // Utility class
    }

    @Data
    public static class CriarHorarioTrabalhoRequest {
        @NotNull(message = "Dia da semana é obrigatório")
        private DiaSemana diaSemana;

        @NotNull(message = "Hora de início é obrigatória")
        private LocalTime horaInicio;

        @NotNull(message = "Hora de fim é obrigatória")
        private LocalTime horaFim;

        private LocalTime inicioPausa; // opcional

        private LocalTime fimPausa; // opcional
    }

    @Data
    public static class AtualizarHorarioTrabalhoRequest {
        @NotNull(message = "Dia da semana é obrigatório")
        private DiaSemana diaSemana;

        @NotNull(message = "Hora de início é obrigatória")
        private LocalTime horaInicio;

        @NotNull(message = "Hora de fim é obrigatória")
        private LocalTime horaFim;

        private LocalTime inicioPausa; // opcional

        private LocalTime fimPausa; // opcional
    }

    @Data
    public static class HorarioTrabalhoResponse {
        private Integer id;
        private DiaSemana diaSemana;
        private LocalTime horaInicio;
        private LocalTime horaFim;
        private LocalTime inicioPausa;
        private LocalTime fimPausa;
        private Integer profissionalId;
        private String profissionalNome;
    }

    @Data
    public static class ListarHorariosTrabalhoResponse {
        private List<HorarioTrabalhoResponse> horarios;
        private int totalElements;
    }
}
