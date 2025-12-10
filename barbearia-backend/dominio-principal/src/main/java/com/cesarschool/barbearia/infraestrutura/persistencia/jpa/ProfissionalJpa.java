package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.dominio.compartilhado.logger.LoggerSingleton;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;
import com.cesarschool.barbearia.dominio.principal.profissional.Agenda;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalRepositorio;
import com.cesarschool.barbearia.dominio.principal.profissional.Senioridade;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "PROFISSIONAL")
public final class ProfissionalJpa {
    
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    @Id
    private Integer id;
    
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "CPF", nullable = false, length = 14)
    private String cpf;

    @Column(name = "TELEFONE", nullable = false, length = 15)
    private String telefone;
    
    @Column(name = "INICIO_JORNADA", nullable = false)
    private LocalTime inicioJornada;

    @Column(name = "FIM_JORNADA", nullable = false)
    private LocalTime fimJornada;
  
    @Enumerated(EnumType.STRING)
    @Column(name = "SENIORIDADE", nullable = false, length = 20)
    private Senioridade senioridade; 

    @Column(name = "ATIVO", nullable = false)
    private boolean ativo; 

    @Column(name = "MOTIVO_INATIVIDADE", length = 255)
    private String motivoInatividade; 

    @ManyToMany
    @JoinTable(
        name = "profissional_servico",
        joinColumns = @JoinColumn(name = "profissional_id"),
        inverseJoinColumns = @JoinColumn(name = "servico_id")
    )
    private List<ServicoOferecidoJpa> servicosOferecidos;
}

interface ProfissionalJpaRepository extends JpaRepository<ProfissionalJpa, Integer> {
    ProfissionalJpa findByCpf(String cpf);
    boolean existsByCpf(String cpf);
    
    @Query("SELECT DISTINCT p FROM ProfissionalJpa p JOIN p.servicosOferecidos s WHERE s.id = :servicoId AND p.ativo = true")
    List<ProfissionalJpa> findByServicoId(@Param("servicoId") Integer servicoId);
    
    @Query(value = "SELECT COUNT(*) FROM profissional_servico WHERE profissional_id = :profissionalId AND servico_id = :servicoId", nativeQuery = true)
    Long countQualificacao(@Param("profissionalId") Integer profissionalId, @Param("servicoId") Integer servicoId);
    
    List<ProfissionalJpa> findByAtivoTrue();
}

@Repository
class ProfissionalJpaRepositorioImpl implements ProfissionalRepositorio {

    private static final LoggerSingleton logger = LoggerSingleton.getInstance();

    @Autowired
    private ProfissionalJpaRepository profissionalJpaRepository;
    
    private ProfissionalJpa toEntity(Profissional dominio) {
        Agenda agenda = dominio.getAgenda() != null ? dominio.getAgenda() : new Agenda();
        
        List<ServicoOferecidoJpa> servicosJpa = new ArrayList<>();
        if (dominio.getServicoOferecidoIds() != null) {
            servicosJpa = dominio.getServicoOferecidoIds().stream()
                .map(idVO -> {
                    ServicoOferecidoJpa s = new ServicoOferecidoJpa();
                    s.setId(idVO.getValor());
                    return s;
                })
                .collect(Collectors.toList());
        }

        return ProfissionalJpa.builder()
            .id(dominio.getId() != null ? dominio.getId().getValor() : null)
            .nome(dominio.getNome())
            .email(dominio.getEmail().getValue()) 
            .cpf(dominio.getCpf().getValue())
            .telefone(dominio.getTelefone().getValue())
            .senioridade(dominio.getSenioridade())
            .ativo(dominio.isAtivo())
            .motivoInatividade(dominio.getMotivoInatividade())
            .inicioJornada(agenda.getInicioJornada() != null ? agenda.getInicioJornada() : LocalTime.of(9, 0))
            .fimJornada(agenda.getFimJornada() != null ? agenda.getFimJornada() : LocalTime.of(18, 0))
            .servicosOferecidos(servicosJpa)
            .build();
    }

    private Profissional toDomain(ProfissionalJpa entity) {
        Agenda agenda = new Agenda();
        agenda.setInicioJornada(entity.getInicioJornada());
        agenda.setFimJornada(entity.getFimJornada());

        List<ServicoOferecidoId> idsServicos = new ArrayList<>();
        if (entity.getServicosOferecidos() != null) {
            idsServicos = entity.getServicosOferecidos().stream()
                .map(s -> new ServicoOferecidoId(s.getId()))
                .collect(Collectors.toList());
        }

        return new Profissional(
            new ProfissionalId(entity.getId()),
            entity.getNome(),
            new Email(entity.getEmail()),
            new Cpf(entity.getCpf()),
            new Telefone(entity.getTelefone()),
            agenda,
            idsServicos,
            entity.getSenioridade(),
            entity.isAtivo(),
            entity.getMotivoInatividade()
        );
    }

    @Override
    public Profissional salvar(Profissional entity) {
        ProfissionalJpa jpa = toEntity(entity);
        ProfissionalJpa saved = profissionalJpaRepository.save(jpa);
        return toDomain(saved);
    }

    @Override
    public Profissional buscarPorId(Integer id) {
        return profissionalJpaRepository.findById(id).map(this::toDomain).orElse(null);
    }

    @Override
    public List<Profissional> listarTodos() {
        return profissionalJpaRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void remover(Integer id) {
        profissionalJpaRepository.deleteById(id);
    }

    @Override
    public Profissional buscarPorCpf(Cpf cpf) {
        ProfissionalJpa jpa = profissionalJpaRepository.findByCpf(cpf.getValue());
        return jpa != null ? toDomain(jpa) : null;
    }

    @Override
    public boolean existePorCpf(Cpf cpf) {
        return profissionalJpaRepository.existsByCpf(cpf.getValue());
    }

    @Override
    public List<Profissional> buscarQualificadosParaServico(ServicoOferecidoId servicoId) {
        return profissionalJpaRepository.findByServicoId(servicoId.getValor())
            .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Profissional> buscarDisponiveisNaDataHora(LocalDateTime dataHora, Integer duracaoMinutos) {
        List<ProfissionalJpa> profissionaisAtivos = profissionalJpaRepository.findByAtivoTrue();
        LocalTime horaInicio = dataHora.toLocalTime();
        LocalTime horaFim = horaInicio.plusMinutes(duracaoMinutos);
        
        List<Profissional> disponiveis = new ArrayList<>();
        
        for (ProfissionalJpa jpa : profissionaisAtivos) {
            boolean dentroDaJornada = 
                !horaInicio.isBefore(jpa.getInicioJornada()) && 
                !horaFim.isAfter(jpa.getFimJornada());
            
            if (dentroDaJornada) {
                disponiveis.add(toDomain(jpa));
            }
        }
        return disponiveis;
    }

    @Override
    public Profissional buscarPrimeiroProfissionalDisponivel(LocalDateTime dataHora, int duracaoServicoMinutos) {
        List<Profissional> disponiveis = buscarDisponiveisNaDataHora(dataHora, duracaoServicoMinutos);
        return disponiveis.isEmpty() ? null : disponiveis.get(0);
    }

    @Override public void adicionarQualificacao(Integer profissionalId, Integer servicoId) {}
    @Override public void removerQualificacao(Integer profissionalId, Integer servicoId) {}
    
    @Override
    public boolean estaQualificado(Integer profissionalId, Integer servicoId) {
        Long count = profissionalJpaRepository.countQualificacao(profissionalId, servicoId);
        return count > 0;
    }

    @Override public boolean possuiAssociacaoServico(String nomeProfissional, String nomeServico) { return false; }
    @Override public void removerAssociacaoServico(String nomeProfissional, String nomeServico) {}
    public void simularAgendamentoAtivo(String nomeServico, boolean ativo) {}
    @Override public boolean temAgendamentoAtivo(String nomeServico) { return false; }
}