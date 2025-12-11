package com.cesarschool.barbearia.aplicacao.agendamento;

import java.time.LocalDateTime;
import java.util.List;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

/**
 * Repositório da camada de aplicação para consultas de agendamento.
 * Retorna DTOs/projeções ao invés de entidades de domínio.
 * Seguindo padrão SGB-2025-01.
 */
public interface AgendamentoRepositorioAplicacao {
    
    /**
     * Busca profissionais disponíveis para um serviço em uma data/hora específica.
     * Retorna apenas profissionais que estão:
     * 1. Qualificados para o serviço
     * 2. Disponíveis no horário (dentro da jornada)
     * 3. Sem conflitos de agendamento
     */
    List<ProfissionalDisponivelResumo> buscarProfissionaisDisponiveis(
        ServicoOferecidoId servicoId, 
        LocalDateTime dataHora, 
        Integer duracaoMinutos
    );
    
    /**
     * Lista agendamentos de um cliente.
     */
    List<AgendamentoResumo> buscarPorCliente(ClienteId clienteId);

    /**
     * Lista todos os agendamentos do sistema.
     */
    List<AgendamentoResumo> listarTodos();
}
