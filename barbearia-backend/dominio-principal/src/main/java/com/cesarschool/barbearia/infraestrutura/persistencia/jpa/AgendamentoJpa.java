package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.agendamento.StatusAgendamento;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
class AgendamentoJpa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Integer id;
    
    @Column(nullable = false)
    LocalDateTime dataHora;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    StatusAgendamento status;
    
    @Column(nullable = false)
    Integer clienteId;
    
    @Column
    Integer profissionalId;
    
    @Column(nullable = false)
    Integer servicoId;
    
    @Column(length = 500)
    String observacoes;
}

interface AgendamentoJpaRepository extends JpaRepository<AgendamentoJpa, Integer> {
    
    @Query("SELECT a FROM AgendamentoJpa a WHERE a.clienteId = :clienteId ORDER BY a.dataHora DESC")
    List<AgendamentoJpa> findByClienteId(@Param("clienteId") Integer clienteId);
    
    @Query("SELECT a FROM AgendamentoJpa a WHERE a.profissionalId = :profissionalId ORDER BY a.dataHora DESC")
    List<AgendamentoJpa> findByProfissionalId(@Param("profissionalId") Integer profissionalId);
    
    @Query("SELECT a FROM AgendamentoJpa a WHERE a.status = :status ORDER BY a.dataHora")
    List<AgendamentoJpa> findByStatus(@Param("status") StatusAgendamento status);
    
    @Query("SELECT a FROM AgendamentoJpa a WHERE a.dataHora BETWEEN :inicio AND :fim ORDER BY a.dataHora")
    List<AgendamentoJpa> findByPeriodo(
        @Param("inicio") LocalDateTime inicio, 
        @Param("fim") LocalDateTime fim
    );
    
    @Query("SELECT COUNT(a) > 0 FROM AgendamentoJpa a " +
           "WHERE a.profissionalId = :profissionalId " +
           "AND a.dataHora = :dataHora " +
           "AND a.status IN ('PENDENTE', 'CONFIRMADO')")
    boolean existeAgendamentoConflitante(
        @Param("profissionalId") Integer profissionalId,
        @Param("dataHora") LocalDateTime dataHora
    );
    
    @Query("SELECT a FROM AgendamentoJpa a " +
           "WHERE a.profissionalId = :profissionalId " +
           "AND DATE(a.dataHora) = DATE(:data) " +
           "AND a.status IN ('PENDENTE', 'CONFIRMADO') " +
           "ORDER BY a.dataHora")
    List<AgendamentoJpa> findAgendamentosDoDia(
        @Param("profissionalId") Integer profissionalId,
        @Param("data") LocalDateTime data
    );
    
    @Query("SELECT a FROM AgendamentoJpa a " +
           "WHERE a.clienteId = :clienteId " +
           "AND a.dataHora >= :agora " +
           "AND a.status IN ('PENDENTE', 'CONFIRMADO') " +
           "ORDER BY a.dataHora")
    List<AgendamentoJpa> findAgendamentosFuturosCliente(
        @Param("clienteId") Integer clienteId,
        @Param("agora") LocalDateTime agora
    );
}

@Repository
class AgendamentoRepositorioImpl implements AgendamentoRepositorio {
    
    @Autowired
    AgendamentoJpaRepository repositorio;
    
    @Autowired
    JpaMapeador mapeador;
    
    @Override
    public Agendamento salvar(Agendamento agendamento) {
        var agendamentoJpa = mapeador.map(agendamento, AgendamentoJpa.class);
        var salvo = repositorio.save(agendamentoJpa);
        return mapeador.map(salvo, Agendamento.class);
    }
    
    @Override
    public Agendamento buscarPorId(Integer id) {
        var agendamentoJpa = repositorio.findById(id)
            .orElse(null);
        return agendamentoJpa != null ? mapeador.map(agendamentoJpa, Agendamento.class) : null;
    }
    
    @Override
    public List<Agendamento> listarTodos() {
        var agendamentosJpa = repositorio.findAll();
        return agendamentosJpa.stream()
            .map(aj -> mapeador.map(aj, Agendamento.class))
            .toList();
    }
    
    @Override
    public void remover(Integer id) {
        repositorio.deleteById(id);
    }
    
    @Override
    public List<Agendamento> buscarPorCliente(ClienteId clienteId) {
        var agendamentosJpa = repositorio.findByClienteId(clienteId.getValor());
        return agendamentosJpa.stream()
            .map(aj -> mapeador.map(aj, Agendamento.class))
            .toList();
    }
    
    @Override
    public List<Agendamento> buscarPorProfissional(ProfissionalId profissionalId) {
        var agendamentosJpa = repositorio.findByProfissionalId(profissionalId.getValor());
        return agendamentosJpa.stream()
            .map(aj -> mapeador.map(aj, Agendamento.class))
            .toList();
    }
    
    @Override
    public List<Agendamento> buscarPorStatus(StatusAgendamento status) {
        var agendamentosJpa = repositorio.findByStatus(status);
        return agendamentosJpa.stream()
            .map(aj -> mapeador.map(aj, Agendamento.class))
            .toList();
    }
    
    @Override
    public List<Agendamento> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        var agendamentosJpa = repositorio.findByPeriodo(inicio, fim);
        return agendamentosJpa.stream()
            .map(aj -> mapeador.map(aj, Agendamento.class))
            .toList();
    }
    
    @Override
    public boolean existeAgendamentoNoPeriodo(
            ProfissionalId profissionalId, 
            LocalDateTime dataHora, 
            int duracaoMinutos) {
        return repositorio.existeAgendamentoConflitante(
            profissionalId.getValor(), 
            dataHora
        );
    }
}
