package com.cesarschool.barbearia.dominio.principal.agendamento;

import java.time.LocalDateTime;
import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.base.Repositorio;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

/**
 * Porta (interface) de persistência para Agendamento.
 */
public interface AgendamentoRepositorio extends Repositorio<Agendamento, Integer> {
    
    List<Agendamento> buscarPorCliente(ClienteId clienteId);
    
    List<Agendamento> buscarPorProfissional(ProfissionalId profissionalId);
    
    List<Agendamento> buscarPorStatus(StatusAgendamento status);
    
    List<Agendamento> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);
        
    boolean existeAgendamentoNoPeriodo(
        ProfissionalId profissionalId, 
        LocalDateTime dataHora, 
        int duracaoMinutos
    );
}
