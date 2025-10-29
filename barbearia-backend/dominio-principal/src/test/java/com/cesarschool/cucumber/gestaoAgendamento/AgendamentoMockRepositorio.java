package com.cesarschool.cucumber.gestaoAgendamento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoId;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.agendamento.StatusAgendamento;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

/**
 * Implementação mock do AgendamentoRepositorio para testes.
 * Simula persistência em memória sem necessidade de banco de dados.
 */
public class AgendamentoMockRepositorio implements AgendamentoRepositorio {
    
    private final Map<Integer, Agendamento> agendamentos = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    
    @Override
    public Agendamento salvar(Agendamento agendamento) {
        if (agendamento.getId() == null || !agendamentos.containsKey(agendamento.getId().getValor())) {
            // Novo agendamento - gera ID
            Integer novoId = idGenerator.getAndIncrement();
            Agendamento novo = Agendamento.builder()
                .id(new AgendamentoId(novoId))
                .dataHora(agendamento.getDataHora())
                .status(agendamento.getStatus())
                .clienteId(agendamento.getClienteId())
                .profissionalId(agendamento.getProfissionalId())
                .servicoId(agendamento.getServicoId())
                .observacoes(agendamento.getObservacoes())
                .build();
            agendamentos.put(novoId, novo);
            return novo;
        } else {
            // Atualiza agendamento existente
            Integer id = agendamento.getId().getValor();
            agendamentos.put(id, agendamento);
            return agendamento;
        }
    }
    
    @Override
    public Agendamento buscarPorId(Integer id) {
        Agendamento agendamento = agendamentos.get(id);
        if (agendamento == null) {
            throw new IllegalArgumentException("Agendamento não encontrado com ID: " + id);
        }
        return agendamento;
    }
    
    @Override
    public List<Agendamento> listarTodos() {
        return new ArrayList<>(agendamentos.values());
    }
    
    @Override
    public void remover(Integer id) {
        agendamentos.remove(id);
    }
    
    @Override
    public List<Agendamento> buscarPorCliente(ClienteId clienteId) {
        return agendamentos.values().stream()
            .filter(a -> a.getClienteId() != null && a.getClienteId().equals(clienteId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Agendamento> buscarPorProfissional(ProfissionalId profissionalId) {
        return agendamentos.values().stream()
            .filter(a -> a.getProfissionalId() != null && a.getProfissionalId().equals(profissionalId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Agendamento> buscarPorStatus(StatusAgendamento status) {
        return agendamentos.values().stream()
            .filter(a -> a.getStatus() == status)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Agendamento> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return agendamentos.values().stream()
            .filter(a -> !a.getDataHora().isBefore(inicio) && !a.getDataHora().isAfter(fim))
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean existeAgendamentoNoPeriodo(
            ProfissionalId profissionalId, 
            LocalDateTime dataHora, 
            int duracaoMinutos) {
        
        LocalDateTime fimAgendamento = dataHora.plusMinutes(duracaoMinutos);
        
        return agendamentos.values().stream()
            .filter(a -> a.getProfissionalId() != null && a.getProfissionalId().equals(profissionalId))
            .filter(a -> a.getStatus() != StatusAgendamento.CANCELADO)
            .anyMatch(a -> {
                LocalDateTime inicioExistente = a.getDataHora();
                LocalDateTime fimExistente = inicioExistente.plusMinutes(duracaoMinutos);
                
                // Verifica sobreposição de horários
                return dataHora.isBefore(fimExistente) && fimAgendamento.isAfter(inicioExistente);
            });
    }
    
    /**
     * Limpa todos os dados do repositório.
     * Útil para resetar estado entre testes.
     */
    public void limparDados() {
        agendamentos.clear();
        idGenerator.set(1);
    }
}
