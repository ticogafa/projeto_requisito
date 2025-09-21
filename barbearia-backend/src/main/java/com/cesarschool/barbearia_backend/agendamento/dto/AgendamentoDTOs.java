package com.cesarschool.barbearia_backend.agendamento.dto;

import java.time.LocalDateTime;


import com.cesarschool.barbearia_backend.common.enums.StatusAgendamento;

import lombok.AllArgsConstructor;
import lombok.Data;

public class AgendamentoDTOs {

    private AgendamentoDTOs() {
        // Utility class
    }

    @Data
    @AllArgsConstructor
    public static class CriarAgendamentoRequest {
        private LocalDateTime dataHora;
        private Integer clienteId;
        private Integer profissionalId;
        private Integer servicoId;
        private String observacoes;
    }

    @Data
    public static class AgendamentoResponse {
        private Integer id;
        private LocalDateTime dataHora;
        private StatusAgendamento status;
        private String observacoes;
        
        // Dados do cliente
        private Integer clienteId;
        private String clienteNome;
        private String clienteEmail;
        private String clienteTelefone;
        
        // Dados do profissional
        private Integer profissionalId;
        private String profissionalNome;
        private String profissionalEmail;
        
        // Dados do serviço
        private Integer servicoId;
        private String servicoNome;
        private java.math.BigDecimal servicoPreco;
        private Integer servicoDuracaoMinutos;
    }

    @Data
    public static class AtualizarAgendamentoRequest {
        private LocalDateTime dataHora;
        private Integer profissionalId;
        private Integer servicoId;
        private String observacoes;
    }

    @Data
    public static class ListarAgendamentosResponse {
        private java.util.List<AgendamentoResponse> agendamentos;
        private int totalElements;
        private int totalPages;
        private int currentPage;
    }
}
