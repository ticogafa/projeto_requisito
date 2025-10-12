package com.cesarschool.barbearia.dominio.agendamento;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;

import com.cesarschool.barbearia.dominio.compartilhado.StatusAgendamento;
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
    private Integer clienteId; // Referência ao Cliente (do dominio-marketing)
    private ProfissionalId profissionalId;
    private ServicoOferecidoId servicoId;
    private String observacoes;

    // Construtor para criação (sem ID)
    public Agendamento(
            LocalDateTime dataHora,
            Integer clienteId,
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
            Integer clienteId,
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
        if (!status.podeSerCancelado()) {
            throw new IllegalStateException("Este agendamento não pode ser cancelado no status atual: " + status);
        }
        this.status = StatusAgendamento.CANCELADO;
    }

    public void iniciarAtendimento() {
        if (!status.podeIniciarAtendimento()) {
            throw new IllegalStateException("O agendamento deve estar confirmado para iniciar atendimento");
        }
        this.status = StatusAgendamento.EM_ATENDIMENTO;
    }

    public void concluir() {
        if (status != StatusAgendamento.EM_ATENDIMENTO) {
            throw new IllegalStateException("Apenas agendamentos em atendimento podem ser concluídos");
        }
        this.status = StatusAgendamento.CONCLUIDO;
    }

    // Getters e Setters com validações
    public AgendamentoId getId() {
        return id;
    }

    public void setId(AgendamentoId id) {
        notNull(id, "Id não pode ser nulo");
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        notNull(dataHora, "Data e hora não podem ser nulos");
        this.dataHora = dataHora;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        notNull(status, "Status não pode ser nulo");
        this.status = status;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setCliente(Integer clienteId) {
        notNull(clienteId, "Cliente não pode ser nulo");
        this.clienteId = clienteId;
    }

    public ProfissionalId getProfissionalId() {
        return profissionalId;
    }

    public void setProfissional(ProfissionalId profissionalId) {
        notNull(profissionalId, "Profissional não pode ser nulo");
        this.profissionalId = profissionalId;
    }

    public ServicoOferecidoId getServicoId() {
        return servicoId;
    }

    public void setServico(ServicoOferecidoId servicoId) {
        notNull(servicoId, "Serviço não pode ser nulo");
        this.servicoId = servicoId;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agendamento)) return false;
        Agendamento that = (Agendamento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Agendamento{" +
                "id=" + id +
                ", dataHora=" + dataHora +
                ", status=" + status +
                ", clienteId=" + clienteId +
                '}';
    }
}
