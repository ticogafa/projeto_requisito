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

    public void cancelar(UsuarioSolicitante usuario) {
        validarStatusCancelamento();
        validarPermissaoDeCancelamento(usuario);
        validarAntecedenciaMinima();
        this.status = StatusAgendamento.CANCELADO;
    }

    private void validarStatusCancelamento() {
        if (!status.podeSerCancelado()) 
            throw new IllegalStateException("Este agendamento não pode ser cancelado no status atual: " + status);
    }

    private void validarPermissaoDeCancelamento(UsuarioSolicitante usuario) {
        boolean autorizado = 
            usuario.isAdmin() ||
            (usuario.isCliente() && this.getClienteId().equals(usuario.getReferenciaId())) ||
            (usuario.isProfissional() && this.getProfissionalId().equals(usuario.getReferenciaId()));

        if (!autorizado)
            throw new IllegalStateException("Usuário não possui permissão para cancelar este agendamento.");
        
    }

    private void validarAntecedenciaMinima() {
        LocalDateTime limiteCancelamento = LocalDateTime.now().plusHours(2);

        if (dataHora.isBefore(limiteCancelamento)) 
            throw new IllegalStateException("Cancelamentos só são permitidos com pelo menos duas horas de antecedência.");
    }

    
    public void setId(AgendamentoId id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do Agendamento");
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
        Validacoes.validarObjetoObrigatorio(clienteId, "ID do Cliente");
        this.clienteId = clienteId;
    }
    
    
    public void setProfissional(ProfissionalId profissionalId) {
        //opcional
        this.profissionalId = profissionalId;
    }
    
    
    public void setServico(ServicoOferecidoId servicoId) {
        Validacoes.validarObjetoObrigatorio(servicoId, "ID do Serviço");
        this.servicoId = servicoId;
    }
    
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
