package com.cesarschool.barbearia_backend.bdd;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.AgendamentoResponse;
import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.CriarAgendamentoRequest;
import com.cesarschool.barbearia_backend.marketing.model.Cliente;
import com.cesarschool.barbearia_backend.profissionais.model.Profissional;
import com.cesarschool.barbearia_backend.profissionais.model.ServicoOferecido;

/**
 * Contexto compartilhado (escopo de cenário) para permitir que diferentes
 * classes de Step Definitions utilizem e modifiquem o mesmo estado sem NPE.
 */
@Component
public class AgendamentoContext {
    public Cliente cliente;
    public Profissional profissional;
    public ServicoOferecido servico;
    public LocalDateTime horarioSelecionado;
    public CriarAgendamentoRequest agendamentoRequest;
    public AgendamentoResponse agendamentoResponse;
    public Exception exception;
}
