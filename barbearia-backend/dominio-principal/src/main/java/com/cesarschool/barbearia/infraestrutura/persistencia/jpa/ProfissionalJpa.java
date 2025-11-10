package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import static jakarta.persistence.GenerationType.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalRepositorio;
import com.cesarschool.barbearia.dominio.principal.profissional.Senioridade;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Builder.Default
    private LocalTime inicioJornada = LocalTime.of(9, 0);

    @Column(name = "FIM_JORNADA", nullable = false)
    @Builder.Default
    private LocalTime fimJornada = LocalTime.of(17, 0);
  
    @Column(name = "SENIORIDADE", nullable = false)
    private Senioridade senioridade; 

    @Column(name = "ATIVO", nullable = false)
    private boolean ativo; 

    @Column(name = "MOTIVO_INATIVIDADE", length = 255)
    private String motivoInatividade; 

    // Em ProfissionalJpa:
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
    
    /**
     * Busca profissionais que oferecem um determinado serviço.
     * @param servicoId ID do serviço
     * @return Lista de profissionais qualificados
     */
    @Query("SELECT DISTINCT p FROM ProfissionalJpa p JOIN p.servicosOferecidos s WHERE s.id = :servicoId AND p.ativo = true")
    List<ProfissionalJpa> findByServicoId(@Param("servicoId") Integer servicoId);
    
    /**
     * Busca todos os profissionais ativos.
     * @return Lista de profissionais ativos
     */
    List<ProfissionalJpa> findByAtivoTrue();
}

@Repository
class ProfissionalJpaRepositorioImpl implements ProfissionalRepositorio {

    @Autowired
    private ProfissionalJpaRepository profissionalJpaRepository;
    
    @Autowired
    private ServicoOferecidoJpaRepository servicoOferecidoJpaRepository;
    
    @Autowired
    private JpaMapeador mapeador;

    @Override
    public Profissional salvar(Profissional entity) {
        ProfissionalJpa jpa = mapeador.map(entity, ProfissionalJpa.class);
        ProfissionalJpa saved = profissionalJpaRepository.save(jpa);
        return mapeador.map(saved, Profissional.class);
    }

    @Override
    public Profissional buscarPorId(Integer id) {
        return profissionalJpaRepository.findById(id)
            .map(jpa -> mapeador.map(jpa, Profissional.class))
            .orElse(null);
    }

    @Override
    public List<Profissional> listarTodos() {
        return profissionalJpaRepository.findAll()
            .stream()
            .map(jpa -> mapeador.map(jpa, Profissional.class))
            .toList();
    }

    @Override
    public void remover(Integer id) {
        profissionalJpaRepository.deleteById(id);
    }

    @Override
    public Profissional buscarPorCpf(Cpf cpf) {
        ProfissionalJpa jpa = profissionalJpaRepository.findByCpf(cpf.getValue());
        return jpa != null ? mapeador.map(jpa, Profissional.class) : null;
    }

    @Override
    public boolean existePorCpf(Cpf cpf) {
        return profissionalJpaRepository.findByCpf(cpf.getValue()) != null;
    }

    @Override
    public List<Profissional> buscarQualificadosParaServico(ServicoOferecidoId servicoId) {
        List<ProfissionalJpa> profissionaisJpa = profissionalJpaRepository.findByServicoId(servicoId.getValor());
        return profissionaisJpa.stream()
            .map(jpa -> mapeador.map(jpa, Profissional.class))
            .toList();
    }

    @Override
    public List<Profissional> buscarDisponiveisNaDataHora(java.time.LocalDateTime dataHora, Integer duracaoMinutos) {
        // Busca todos os profissionais ativos
        List<ProfissionalJpa> profissionaisAtivos = profissionalJpaRepository.findByAtivoTrue();
        
        // Filtra por horário de trabalho
        LocalTime horaInicio = dataHora.toLocalTime();
        LocalTime horaFim = horaInicio.plusMinutes(duracaoMinutos);
        
        List<Profissional> disponiveis = new ArrayList<>();
        
        for (ProfissionalJpa jpa : profissionaisAtivos) {
            // Verifica se o horário solicitado está dentro da jornada do profissional
            boolean dentroDaJornada = 
                !horaInicio.isBefore(jpa.getInicioJornada()) && 
                !horaFim.isAfter(jpa.getFimJornada());
            
            if (dentroDaJornada) {
                // TODO: Verificar agendamentos existentes para esse profissional nesse horário
                // Por enquanto, assume que está disponível se estiver na jornada
                disponiveis.add(mapeador.map(jpa, Profissional.class));
            }
        }
        
        return disponiveis;
    }

    @Override
    public Profissional buscarPrimeiroProfissionalDisponivel(java.time.LocalDateTime dataHora, int duracaoServicoMinutos) {
        List<Profissional> disponiveis = buscarDisponiveisNaDataHora(dataHora, duracaoServicoMinutos);
        return disponiveis.isEmpty() ? null : disponiveis.get(0);
    }

    @Override
    public void adicionarQualificacao(Integer profissionalId, Integer servicoId) {
        ProfissionalJpa profissional = profissionalJpaRepository.findById(profissionalId)
            .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado: " + profissionalId));
        
        ServicoOferecidoJpa servico = servicoOferecidoJpaRepository.findById(servicoId)
            .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado: " + servicoId));
        
        if (!profissional.getServicosOferecidos().contains(servico)) {
            profissional.getServicosOferecidos().add(servico);
            profissionalJpaRepository.save(profissional);
        }
    }

    @Override
    public void removerQualificacao(Integer profissionalId, Integer servicoId) {
        ProfissionalJpa profissional = profissionalJpaRepository.findById(profissionalId)
            .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado: " + profissionalId));
        
        profissional.getServicosOferecidos().removeIf(s -> s.getId().equals(servicoId));
        profissionalJpaRepository.save(profissional);
    }

    @Override
    public boolean estaQualificado(Integer profissionalId, Integer servicoId) {
        ProfissionalJpa profissional = profissionalJpaRepository.findById(profissionalId)
            .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado: " + profissionalId));
        
        return profissional.getServicosOferecidos().stream()
            .anyMatch(s -> s.getId().equals(servicoId));
    }
}
