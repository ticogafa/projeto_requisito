package com.cesarschool.barbearia.dominio.principal.agendamento;

import java.time.LocalDateTime;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

import lombok.Builder;
import lombok.Getter;

/**
 * Entidade de domínio representando um Agendamento.
 * Adaptado do código original - sem anotações JPA.
 */
@Getter
public final class Agendamento {
    private AgendamentoId id;
    private LocalDateTime dataHora;
    private StatusAgendamento status;
    private ClienteId clienteId; // Referência ao dominio-marketing
    private ProfissionalId profissionalId; // Referência ao dominio-profissionais
    private ServicoOferecidoId servicoId; // Referência ao dominio-profissionais
    private String observacoes;

    @Builder
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

    @Builder
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
        if (!status.podeSerCancelado()) {
            throw new IllegalStateException(
                "Este agendamento não pode ser cancelado no status atual: " + status);
        }
        // Verifica se falta menos de 2 horas para o agendamento
        if (dataHora.isBefore(LocalDateTime.now().plusHours(2))) {
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
        //opcional
        this.profissionalId = profissionalId;
    }
    
    
    public void setServico(ServicoOferecidoId servicoId) {
        this.servicoId = servicoId;
    }
    
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
