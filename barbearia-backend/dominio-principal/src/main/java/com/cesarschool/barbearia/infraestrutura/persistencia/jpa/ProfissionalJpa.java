package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import static jakarta.persistence.GenerationType.*;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalRepositorio;
import com.cesarschool.barbearia.dominio.principal.profissional.Senioridade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
}

interface ProfissionalJpaRepository extends JpaRepository<ProfissionalJpa, Integer> {
    ProfissionalJpa findByCpf(String cpf);
}

@Repository
class ProfissionalJpaRepositorioImpl implements ProfissionalRepositorio {

    @Autowired
    private ProfissionalJpaRepository profissionalJpaRepository;
    
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
    public Profissional buscarPrimeiroProfissionalDisponivel(java.time.LocalDateTime dataHora, int duracaoServicoMinutos) {
        // TODO: Implementar lógica de disponibilidade
        return null;
    }
}
