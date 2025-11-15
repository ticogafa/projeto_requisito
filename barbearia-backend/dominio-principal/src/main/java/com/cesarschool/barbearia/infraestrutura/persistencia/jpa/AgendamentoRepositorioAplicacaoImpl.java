package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.aplicacao.agendamento.AgendamentoRepositorioAplicacao;
import com.cesarschool.barbearia.aplicacao.agendamento.AgendamentoResumo;
import com.cesarschool.barbearia.aplicacao.agendamento.ProfissionalDisponivelResumo;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

/**
 * Implementação JPA do repositório de aplicação para agendamentos.
 * Usa queries nativas/JPQL para retornar projeções diretamente.
 * Seguindo padrão SGB-2025-01.
 */
@Repository
class AgendamentoRepositorioAplicacaoImpl implements AgendamentoRepositorioAplicacao {

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
        
        System.out.println("=== DEBUG REPOSITORIO APLICACAO ===");
        System.out.println("servicoId: " + servicoId.getValor());
        System.out.println("horaInicio: " + horaInicio);
        System.out.println("horaFim: " + horaFim);
        System.out.println("duracaoMinutos: " + duracaoMinutos);
        
        var resultado = profissionalDisponivelRepo.buscarDisponiveis(
            servicoId.getValor(),
            horaInicio,
            horaFim
        );
        
        System.out.println("Resultado query: " + resultado.size() + " profissionais encontrados");
        return resultado;
    }

    @Override
    public List<AgendamentoResumo> buscarPorCliente(ClienteId clienteId) {
        return agendamentoResumoRepo.buscarPorCliente(clienteId.getValor());
    }
}

/**
 * Repository para consultas de profissionais disponíveis.
 * Usa projeção de interface Spring Data JPA.
 */
@Repository
interface ProfissionalDisponivelQueryRepository extends org.springframework.data.jpa.repository.JpaRepository<ProfissionalJpa, Integer> {
    
    /**
     * Busca profissionais disponíveis usando projeção.
     * Retorna apenas profissionais que:
     * 1. Estão qualificados para o serviço (via profissional_servico)
     * 2. Estão dentro da jornada de trabalho
     * Por enquanto não verifica conflitos de agendamento (TODO).
     */
    @Query("""
        SELECT DISTINCT p.id as id, p.nome as nome, p.senioridade as senioridade
        FROM ProfissionalJpa p
        INNER JOIN p.servicosOferecidos s
        WHERE s.id = :servicoId
        AND p.ativo = true
        AND :horaInicio >= p.inicioJornada
        AND :horaFim <= p.fimJornada
        ORDER BY p.senioridade DESC, p.nome
        """)
    List<ProfissionalDisponivelResumo> buscarDisponiveis(
        @Param("servicoId") Integer servicoId,
        @Param("horaInicio") java.time.LocalTime horaInicio,
        @Param("horaFim") java.time.LocalTime horaFim
    );
}

/**
 * Repository para consultas de resumos de agendamento.
 * Usa projeção de interface Spring Data JPA.
 */
@Repository
interface AgendamentoResumoQueryRepository extends org.springframework.data.jpa.repository.JpaRepository<AgendamentoJpa, Integer> {
    
    @Query("""
        SELECT a.id as id,
               a.dataHora as dataHora,
               COALESCE(p.nome, 'Aguardando confirmação') as profissionalNome,
               s.nome as servicoNome,
               a.status as status,
               a.observacoes as observacoes
        FROM AgendamentoJpa a
        LEFT JOIN ProfissionalJpa p ON p.id = a.profissionalId
        INNER JOIN ServicoOferecidoJpa s ON s.id = a.servicoId
        WHERE a.clienteId = :clienteId
        ORDER BY a.dataHora DESC
        """)
    List<AgendamentoResumo> buscarPorCliente(@Param("clienteId") Integer clienteId);
}
