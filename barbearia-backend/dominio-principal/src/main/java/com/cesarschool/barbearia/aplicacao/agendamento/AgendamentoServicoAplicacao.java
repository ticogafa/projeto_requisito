package com.cesarschool.barbearia.aplicacao.agendamento;

import static org.apache.commons.lang3.Validate.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoServico;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoServico;

/**
 * Serviço da camada de aplicação para agendamentos.
 * Orquestra serviços de domínio e repositórios de aplicação.
 * Seguindo padrão SGB-2025-01.
 */
public class AgendamentoServicoAplicacao {
    
    private final AgendamentoRepositorioAplicacao repositorioAplicacao;
    private final AgendamentoServico agendamentoServico;
    private final ServicoOferecidoServico servicoServico;

    public AgendamentoServicoAplicacao(
            AgendamentoRepositorioAplicacao repositorioAplicacao,
            AgendamentoServico agendamentoServico,
            ServicoOferecidoServico servicoServico) {
        notNull(repositorioAplicacao, "AgendamentoRepositorioAplicacao não pode ser nulo");
        notNull(agendamentoServico, "AgendamentoServico não pode ser nulo");
        notNull(servicoServico, "ServicoOferecidoServico não pode ser nulo");
        
        this.repositorioAplicacao = repositorioAplicacao;
        this.agendamentoServico = agendamentoServico;
        this.servicoServico = servicoServico;
    }

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
        
        // Criar agendamento usando Builder
        Agendamento agendamento = Agendamento.builder()
                .dataHora(request.getDataHora())
                .clienteId(new ClienteId(request.getClienteId()))
                .profissionalId(profId)
                .servicoId(new ServicoOferecidoId(request.getServicoId()))
                .observacoes(request.getObservacoes())
                .build();
        
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
}
