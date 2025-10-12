package com.cesarschool.barbearia.dominio.agendamento;

import java.time.LocalDateTime;

import com.cesarschool.barbearia.dominio.compartilhado.enums.StatusAgendamento;
import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.marketing.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.profissionais.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.profissionais.servico.ServicoOferecidoId;

/**
 * Entidade de domínio representando um Agendamento.
 * Adaptado do código original - sem anotações JPA.
 */
public final class Agendamento {
    private AgendamentoId id;
    private LocalDateTime dataHora;
    private StatusAgendamento status;
    private ClienteId clienteId; // Referência ao dominio-marketing
    private ProfissionalId profissionalId; // Referência ao dominio-profissionais
    private ServicoOferecidoId servicoId; // Referência ao dominio-profissionais
    private String observacoes;

    // Construtor para criação (sem ID)
    public Agendamento(
            LocalDateTime dataHora,
            ClienteId clienteId,
            ProfissionalId profissionalId,
            ServicoOferecidoId servicoId,
            String observacoes) {
        setDataHora(dataHora);
        setCliente(clienteId);
        setProfissional(profissionalId);
        setServico(servicoId);
        setObservacoes(observacoes);
        this.status = StatusAgendamento.PENDENTE;
    }

    // Construtor para reconstrução (com ID)
    public Agendamento(
            AgendamentoId id,
            LocalDateTime dataHora,
            StatusAgendamento status,
            ClienteId clienteId,
            ProfissionalId profissionalId,
            ServicoOferecidoId servicoId,
            String observacoes) {
        this(dataHora, clienteId, profissionalId, servicoId, observacoes);
        setId(id);
        setStatus(status);
    }

    // Métodos de negócio
    public void confirmar() {
        if (status != StatusAgendamento.PENDENTE) {
            throw new IllegalStateException("Apenas agendamentos pendentes podem ser confirmados");
        }
        this.status = StatusAgendamento.CONFIRMADO;
    }

    public void cancelar() {
        if (!status.podeSerCancelado() && LocalDateTime.now().compareTo(getDataHora()) <= 2) {
            throw new IllegalStateException(
                "Este agendamento não pode ser cancelado no status atual: " + status);
        }
        if (dataHora.plusHours(2).isBefore(LocalDateTime.now())) {
            throw new IllegalStateException(
            "Cancelamentos só são permitidos com duas horas de antecedência."
            );
        }
        this.status = StatusAgendamento.CANCELADO;
    }

    
    public void setId(AgendamentoId id) {
        this.id = id;
    }
    
    
    public void setDataHora(LocalDateTime dataHora) {
        Validacoes.validarObjetoObrigatorio(dataHora, "Data e hora");
        this.dataHora = dataHora;
    }
    
    public void setStatus(StatusAgendamento status) {
        Validacoes.validarObjetoObrigatorio(status, "Status");
        this.status = status;
    }
    
    public void setCliente(ClienteId clienteId) {
        this.clienteId = clienteId;
    }
    
    
    public void setProfissional(ProfissionalId profissionalId) {
        this.profissionalId = profissionalId;
    }
    
    
    public void setServico(ServicoOferecidoId servicoId) {
        this.servicoId = servicoId;
    }
    
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    // Getters
    public AgendamentoId getId() { return id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public StatusAgendamento getStatus() { return status; }
    public ClienteId getClienteId() { return clienteId; }
    public ProfissionalId getProfissionalId() { return profissionalId; }
    public ServicoOferecidoId getServicoId() { return servicoId; }
    public String getObservacoes() { return observacoes; }

}
