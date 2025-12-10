package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.aplicacao.servico.ServicoOferecidoResumo;
import com.cesarschool.barbearia.aplicacao.servico.ServicoOferecidolRepositorioAplicacao;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoRepositorio;

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
@Table(name = "SERVICO_OFERECIDO")
class ServicoOferecidoJpa {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NOME", nullable = false, length = 100, unique = true)
    private String nome;

    @Column(name = "PRECO", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "DESCRICAO", nullable = false, length = 500)
    private String descricao;

    @Column(name = "DURACAO_MINUTOS", nullable = false)
    private Integer duracaoMinutos;

    @Column(name = "ATIVO", nullable = false)
    private boolean ativo;

    @Column(name = "MOTIVO_INATIVIDADE")
    private String motivoInatividade;
}

interface ServicoOferecidoJpaRepository extends JpaRepository<ServicoOferecidoJpa, Integer> {
    
    ServicoOferecidoJpa findByNome(String nome);
    
    List<ServicoOferecidoResumo> findServicoOferecidoResumoByOrderByNome();

    @Query(value = "SELECT COUNT(*) > 0 FROM profissional_servico ps " +
                   "JOIN profissional p ON p.id = ps.profissional_id " +
                   "JOIN servico_oferecido s ON s.id = ps.servico_id " +
                   "WHERE s.nome = :nomeServico AND p.nome = :nomeProfissional", nativeQuery = true)
    boolean existsByNomeServicoAndNomeProfissional(
        @Param("nomeServico") String nomeServico, 
        @Param("nomeProfissional") String nomeProfissional
    );
}

@Repository
class ServicoOferecidoJpaRepositorioImpl implements ServicoOferecidoRepositorio, ServicoOferecidolRepositorioAplicacao {

    @Autowired
    private ServicoOferecidoJpaRepository jpaRepository;
    
    private ServicoOferecidoJpa toEntity(ServicoOferecido dominio) {
        return ServicoOferecidoJpa.builder()
            .id(dominio.getId() != null ? dominio.getId().getValor() : null)
            .nome(dominio.getNome())
            .preco(dominio.getPreco())
            .descricao(dominio.getDescricao())
            .duracaoMinutos(dominio.getDuracaoMinutos())
            .ativo(dominio.isAtivo())
            .motivoInatividade(dominio.getMotivoInatividade())
            .build();
    }

    private ServicoOferecido toDomain(ServicoOferecidoJpa entity) {
        if (entity == null) return null;
        
        ServicoOferecido dominio = new ServicoOferecido(
            new ServicoOferecidoId(entity.getId()),
            entity.getNome(),
            entity.getPreco(),
            entity.getDescricao(),
            entity.getDuracaoMinutos()
        );
        
        if (!entity.isAtivo()) {
            dominio.desativar(entity.getMotivoInatividade());
        }
        
        return dominio;
    }

    @Override
    public ServicoOferecido salvar(ServicoOferecido entity) {
        ServicoOferecidoJpa jpa = toEntity(entity);
        ServicoOferecidoJpa salvo = jpaRepository.save(jpa);
        return toDomain(salvo);
    }

    @Override
    public List<ServicoOferecido> listarTodos() {
        return jpaRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<ServicoOferecidoResumo> listarTodosResumos() {
        return jpaRepository.findServicoOferecidoResumoByOrderByNome();
    }

    @Override
    public ServicoOferecido buscarPorNome(String nome) {
        return toDomain(jpaRepository.findByNome(nome));
    }

    @Override
    public ServicoOferecido buscarPorId(Integer id) {
        return jpaRepository.findById(id).map(this::toDomain).orElse(null);
    }

    @Override
    public void remover(Integer id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean estaQualificado(String nomeServico, String nomeProfissional) {
        return jpaRepository.existsByNomeServicoAndNomeProfissional(nomeServico, nomeProfissional);
    }

    @Override
    public void salvarAssociacao(String nomeServico, String nomeProfissional) {
    }

    @Override
    public boolean isAtivo(Integer servicoId) {
        return jpaRepository.findById(servicoId)
                .map(ServicoOferecidoJpa::isAtivo)
                .orElse(false);
    }
}