package com.cesarschool.barbearia_backend.agendamento.dto;

import java.time.LocalDateTime;


import com.cesarschool.barbearia_backend.common.enums.StatusAgendamento;
import com.cesarschool.barbearia_backend.marketing.dto.ClienteDTOs.ClienteResponse;
import com.cesarschool.barbearia_backend.profissionais.dto.ProfissionalDTOs.ProfissionalResponse;
import com.cesarschool.barbearia_backend.profissionais.dto.ServicoDTOs.ServicoResponse;

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
        private ClienteResponse cliente;
        private ProfissionalResponse profissional;
        private ServicoResponse servico;
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
