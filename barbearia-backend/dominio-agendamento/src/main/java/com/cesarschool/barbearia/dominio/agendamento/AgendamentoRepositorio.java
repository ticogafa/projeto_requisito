package com.cesarschool.barbearia.dominio.agendamento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.cesarschool.barbearia.dominio.compartilhado.StatusAgendamento;
import com.cesarschool.barbearia.dominio.profissionais.profissional.ProfissionalId;

/**
 * Porta (interface) de persistência para Agendamento.
 */
public interface AgendamentoRepositorio {
    
    Agendamento salvar(Agendamento agendamento);
    
    Optional<Agendamento> buscarPorId(AgendamentoId id);
    
    List<Agendamento> buscarPorCliente(Integer clienteId);
    
    List<Agendamento> buscarPorProfissional(ProfissionalId profissionalId);
    
    List<Agendamento> buscarPorStatus(StatusAgendamento status);
    
    List<Agendamento> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);
    
    List<Agendamento> listarTodos();
    
    void remover(AgendamentoId id);
    
    boolean existeAgendamentoNoPeriodo(
        ProfissionalId profissionalId, 
        LocalDateTime dataHora, 
        int duracaoMinutos
    );
}
