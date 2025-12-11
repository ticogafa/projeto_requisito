package com.cesarschool.barbearia.aplicacao.agendamento;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoServico;
import com.cesarschool.barbearia.dominio.principal.agendamento.StatusAgendamento;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoServico;

import lombok.RequiredArgsConstructor;

/**
 * Serviço da camada de aplicação para agendamentos.
 * Orquestra serviços de domínio e repositórios de aplicação.
 * Seguindo padrão SGB-2025-01.
 */
@RequiredArgsConstructor
public class AgendamentoServicoAplicacao {
    
    private final AgendamentoRepositorioAplicacao repositorioAplicacao;
    private final AgendamentoServico agendamentoServico;
    private final ServicoOferecidoServico servicoServico;

    /**
     * Busca profissionais disponíveis para um serviço em uma data/hora.
     */
    public List<ProfissionalDisponivelResumo> buscarProfissionaisDisponiveis(
            Integer servicoId, 
            LocalDateTime dataHora) {
        
        ServicoOferecido servico = servicoServico.buscarPorId(servicoId);
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado: " + servicoId);
        }
        
        return repositorioAplicacao.buscarProfissionaisDisponiveis(
            new ServicoOferecidoId(servicoId),
            dataHora,
            servico.getDuracaoMinutos()
        );
    }

    /**
     * Cria um novo agendamento.
     */
    public AgendamentoResumo criar(CriarAgendamentoRequest request) {
        notNull(request, "Request não pode ser nulo");
        
        // Validar horário de funcionamento (8h às 18h)
        LocalTime hora = request.getDataHora().toLocalTime();
        if (hora.isBefore(LocalTime.of(8, 0)) || hora.isAfter(LocalTime.of(18, 0))) {
            throw new IllegalStateException(
                "Agendamentos só podem ser feitos entre 08:00 e 18:00"
            );
        }
        
        // Buscar duração do serviço
        ServicoOferecido servico = servicoServico.buscarPorId(request.getServicoId());
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado: " + request.getServicoId());
        }
        
        // Criar profissionalId se fornecido
        ProfissionalId profId = request.getProfissionalId() != null ? 
                new ProfissionalId(request.getProfissionalId()) : null;
        
        // Criar agendamento SEM ID (será gerado pelo banco)
        Agendamento agendamento = new Agendamento(
                request.getDataHora(),
                new ClienteId(request.getClienteId()),
                profId,
                new ServicoOferecidoId(request.getServicoId()),
                request.getObservacoes()
        );
        
        // Salvar via serviço de domínio
        Agendamento criado = agendamentoServico.criar(agendamento, servico.getDuracaoMinutos());
        
        // Buscar e retornar com dados completos via repositório de aplicação
        return repositorioAplicacao.buscarPorCliente(new ClienteId(request.getClienteId()))
            .stream()
            .filter(a -> a.getId().equals(criado.getId().getValor()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Erro ao buscar agendamento criado"));
    }

    /**
     * Lista agendamentos de um cliente.
     */
    public List<AgendamentoResumo> listarPorCliente(Integer clienteId) {
        return repositorioAplicacao.buscarPorCliente(new ClienteId(clienteId));
    }

    /**
     * Lista todos os agendamentos do sistema.
     */
    public List<AgendamentoResumo> listarTodos() {
        return repositorioAplicacao.listarTodos();
    }

    /**
     * Edita um agendamento existente.
     */
    public AgendamentoResumo editar(Integer agendamentoId, EditarAgendamentoRequest request) {
        notNull(request, "Request não pode ser nulo");
        notNull(agendamentoId, "ID do agendamento não pode ser nulo");
        
        // Se estiver apenas mudando o status para CONCLUIDO, não validar horário
        if (request.getStatus() != null && request.getStatus() == StatusAgendamento.CONCLUIDO) {
            // Apenas concluir o agendamento
            Agendamento concluido = agendamentoServico.concluir(
                new com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoId(agendamentoId)
            );
            
            return repositorioAplicacao.buscarPorCliente(concluido.getClienteId())
                .stream()
                .filter(a -> a.getId().equals(concluido.getId().getValor()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));
        }
        
        // Validar horário de funcionamento
        LocalTime hora = request.getDataHora().toLocalTime();
        if (hora.isBefore(LocalTime.of(8, 0)) || hora.isAfter(LocalTime.of(18, 0))) {
            throw new IllegalStateException(
                "Agendamentos só podem ser feitos entre 08:00 e 18:00"
            );
        }
        
        ProfissionalId profId = request.getProfissionalId() != null ? 
                new ProfissionalId(request.getProfissionalId()) : null;
        
        // Editar via serviço de domínio
        Agendamento editado = agendamentoServico.editar(
            new com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoId(agendamentoId),
            request.getDataHora(),
            profId,
            request.getObservacoes()
        );
        
        // Retornar com dados completos
        return repositorioAplicacao.buscarPorCliente(editado.getClienteId())
            .stream()
            .filter(a -> a.getId().equals(editado.getId().getValor()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Erro ao buscar agendamento editado"));
    }

    /**
     * Cancela um agendamento.
     */
    public AgendamentoResumo cancelar(Integer agendamentoId, Integer clienteId) {
        notNull(agendamentoId, "ID do agendamento não pode ser nulo");
        notNull(clienteId, "ID do cliente não pode ser nulo");
        
        // Criar usuario solicitante como cliente
        com.cesarschool.barbearia.dominio.principal.agendamento.UsuarioSolicitante usuario = 
            new com.cesarschool.barbearia.dominio.principal.agendamento.UsuarioSolicitante(
                com.cesarschool.barbearia.dominio.compartilhado.enums.TipoUsuario.CLIENTE,
                new ClienteId(clienteId)
            );
        
        // Cancelar via serviço de domínio
        Agendamento cancelado = agendamentoServico.cancelar(
            new com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoId(agendamentoId),
            usuario
        );
        
        // Retornar com dados completos
        return repositorioAplicacao.buscarPorCliente(cancelado.getClienteId())
            .stream()
            .filter(a -> a.getId().equals(cancelado.getId().getValor()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Erro ao buscar agendamento cancelado"));
    }
}
