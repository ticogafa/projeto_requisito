package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.aplicacao.agendamento.AgendamentoRepositorioAplicacao;
import com.cesarschool.barbearia.aplicacao.agendamento.AgendamentoResumo;
import com.cesarschool.barbearia.aplicacao.agendamento.ProfissionalDisponivelResumo;
import com.cesarschool.barbearia.dominio.compartilhado.logger.LoggerSingleton;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

/**
 * Implementação JPA do repositório de aplicação para agendamentos.
 * Usa queries nativas/JPQL para retornar projeções diretamente.
 * Seguindo padrão SGB-2025-01.
 */
@Repository
class AgendamentoRepositorioAplicacaoImpl implements AgendamentoRepositorioAplicacao {

    private static final LoggerSingleton logger = LoggerSingleton.getInstance();

    @Autowired
    private ProfissionalDisponivelQueryRepository profissionalDisponivelRepo;
    
    @Autowired
    private AgendamentoResumoQueryRepository agendamentoResumoRepo;

    @Override
    public List<ProfissionalDisponivelResumo> buscarProfissionaisDisponiveis(
            ServicoOferecidoId servicoId,
            LocalDateTime dataHora,
            Integer duracaoMinutos) {
        
        var horaInicio = dataHora.toLocalTime();
        var horaFim = dataHora.toLocalTime().plusMinutes(duracaoMinutos);
        var inicioDia = dataHora.toLocalDate().atStartOfDay();
        var fimDia = dataHora.toLocalDate().atTime(LocalTime.MAX);
        
        logger.info("Buscando profissionais disponíveis - servicoId: " + servicoId.getValor() + 
                   ", data: " + dataHora.toLocalDate() +
                   ", horario: " + horaInicio + " as " + horaFim + 
                   ", duração: " + duracaoMinutos + "min");
        
        // 1. Buscar todos os profissionais qualificados e ativos (Entidades completas para acessar jornada)
        List<ProfissionalJpa> candidatos = profissionalDisponivelRepo.buscarCandidatos(
            servicoId.getValor()
        );
        
        logger.info("Encontrados " + candidatos.size() + " profissionais qualificados.");

        // 2. Buscar agendamentos do dia para verificar conflitos
        List<AgendamentoConflitoInfo> agendamentosDoDia = profissionalDisponivelRepo.buscarAgendamentosDoDia(
            inicioDia, fimDia
        );

        // 3. Filtrar na memória
        List<ProfissionalDisponivelResumo> disponiveis = candidatos.stream()
            .filter(p -> {
                // A. Verificar Jornada de Trabalho
                if (horaInicio.isBefore(p.getInicioJornada()) || horaFim.isAfter(p.getFimJornada())) {
                    return false;
                }
                
                // B. Verificar Conflitos
                boolean temConflito = agendamentosDoDia.stream()
                    .filter(a -> a.getProfissionalId().equals(p.getId()))
                    .anyMatch(a -> {
                        LocalDateTime aInicio = a.getDataHora();
                        LocalDateTime aFim = aInicio.plusMinutes(a.getDuracao());
                        
                        LocalDateTime reqInicio = dataHora;
                        LocalDateTime reqFim = reqInicio.plusMinutes(duracaoMinutos);
                        
                        // Overlap logic: (StartA < EndB) and (EndA > StartB)
                        return reqInicio.isBefore(aFim) && reqFim.isAfter(aInicio);
                    });
                
                return !temConflito;
            })
            .map(p -> new ProfissionalResumoImpl(
                p.getId(), 
                p.getNome(), 
                p.getSenioridade().name()
            ))
            .collect(Collectors.toList());
        
        logger.success("Retornando " + disponiveis.size() + " profissionais disponíveis após filtros.");
        return disponiveis;
    }

    @Override
    public List<AgendamentoResumo> buscarPorCliente(ClienteId clienteId) {
        return agendamentoResumoRepo.buscarPorCliente(clienteId.getValor());
    }

    @Override
    public List<AgendamentoResumo> buscarPorProfissional(ProfissionalId profissionalId) {
        return agendamentoResumoRepo.buscarPorProfissional(profissionalId.getValor());
    }

    @Override
    public List<AgendamentoResumo> listarTodos() {
        return agendamentoResumoRepo.listarTodos();
    }
    
    // Implementação interna do DTO para retorno
    private record ProfissionalResumoImpl(Integer id, String nome, String senioridade) implements ProfissionalDisponivelResumo {
        @Override public Integer getId() { return id; }
        @Override public String getNome() { return nome; }
        @Override public String getSenioridade() { return senioridade; }
    }
}

/**
 * Interface para projeção de informações de conflito
 */
interface AgendamentoConflitoInfo {
    Integer getProfissionalId();
    LocalDateTime getDataHora();
    Integer getDuracao();
}

/**
 * Repository para consultas de profissionais disponíveis e verificação de conflitos.
 */
@Repository
interface ProfissionalDisponivelQueryRepository extends JpaRepository<ProfissionalJpa, Integer> {
    
    @Query("""
        SELECT DISTINCT p
        FROM ProfissionalJpa p
        INNER JOIN p.servicosOferecidos s
        WHERE s.id = :servicoId
        AND p.ativo = true
        ORDER BY p.senioridade DESC, p.nome
        """)
    List<ProfissionalJpa> buscarCandidatos(@Param("servicoId") Integer servicoId);

    @Query("""
        SELECT a.profissionalId as profissionalId, a.dataHora as dataHora, s.duracaoMinutos as duracao
        FROM AgendamentoJpa a
        JOIN ServicoOferecidoJpa s ON s.id = a.servicoId
        WHERE a.dataHora >= :inicioDia AND a.dataHora <= :fimDia
        AND a.status IN ('PENDENTE', 'CONFIRMADO')
        """)
    List<AgendamentoConflitoInfo> buscarAgendamentosDoDia(
        @Param("inicioDia") LocalDateTime inicioDia,
        @Param("fimDia") LocalDateTime fimDia
    );
}

/**
 * Repository para consultas de resumos de agendamento.
 * Usa projeção de interface Spring Data JPA.
 */
@Repository
interface AgendamentoResumoQueryRepository extends JpaRepository<AgendamentoJpa, Integer> {
    
    @Query("""
        SELECT a.id as id,
               a.dataHora as dataHora,
               a.profissionalId as profissionalId,
               COALESCE(p.nome, 'Aguardando confirmação') as profissionalNome,
               a.clienteId as clienteId,
               c.nome as clienteNome,
               a.servicoId as servicoId,
               s.nome as servicoNome,
               s.preco as servicoPreco,
               a.status as status,
               a.observacoes as observacoes
        FROM AgendamentoJpa a
        LEFT JOIN ProfissionalJpa p ON p.id = a.profissionalId
        INNER JOIN ServicoOferecidoJpa s ON s.id = a.servicoId
        INNER JOIN ClienteJpa c ON c.id = a.clienteId
        WHERE a.clienteId = :clienteId
        ORDER BY a.dataHora DESC
        """)
    List<AgendamentoResumo> buscarPorCliente(@Param("clienteId") Integer clienteId);

    @Query("""
        SELECT a.id as id,
               a.dataHora as dataHora,
               a.profissionalId as profissionalId,
               p.nome as profissionalNome,
               a.servicoId as servicoId,
               s.nome as servicoNome,
               a.clienteId as clienteId,
               c.nome as clienteNome,
               a.status as status,
               a.observacoes as observacoes
        FROM AgendamentoJpa a
        JOIN ProfissionalJpa p ON p.id = a.profissionalId
        JOIN ServicoOferecidoJpa s ON s.id = a.servicoId
        JOIN ClienteJpa c ON c.id = a.clienteId
        WHERE a.profissionalId = :profissionalId
        ORDER BY a.dataHora DESC
        """)
    List<AgendamentoResumo> buscarPorProfissional(@Param("profissionalId") Integer profissionalId);

    @Query("""
        SELECT a.id as id,
               a.dataHora as dataHora,
               a.clienteId as clienteId,
               c.nome as clienteNome,
               a.profissionalId as profissionalId,
               COALESCE(p.nome, 'Aguardando confirmação') as profissionalNome,
               a.servicoId as servicoId,
               s.nome as servicoNome,
               s.preco as servicoPreco,
               a.status as status,
               a.observacoes as observacoes
        FROM AgendamentoJpa a
        LEFT JOIN ProfissionalJpa p ON p.id = a.profissionalId
        INNER JOIN ServicoOferecidoJpa s ON s.id = a.servicoId
        LEFT JOIN ClienteJpa c ON c.id = a.clienteId
        ORDER BY a.dataHora DESC
        """)
    List<AgendamentoResumo> listarTodos();
}
