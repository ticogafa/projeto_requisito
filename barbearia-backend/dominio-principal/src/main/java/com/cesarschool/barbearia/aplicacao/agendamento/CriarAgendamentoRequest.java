package com.cesarschool.barbearia.aplicacao.agendamento;

import java.time.LocalDateTime;

/**
 * DTO para requisição de criação de agendamento.
 */
public class CriarAgendamentoRequest {
    private Integer clienteId;
    private Integer servicoId;
    private LocalDateTime dataHora;
    private Integer profissionalId; // Opcional - se null, sistema escolhe automaticamente
    private String observacoes;

    // Getters e Setters
    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getServicoId() {
        return servicoId;
    }

    public void setServicoId(Integer servicoId) {
        this.servicoId = servicoId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Integer getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Integer profissionalId) {
        this.profissionalId = profissionalId;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
