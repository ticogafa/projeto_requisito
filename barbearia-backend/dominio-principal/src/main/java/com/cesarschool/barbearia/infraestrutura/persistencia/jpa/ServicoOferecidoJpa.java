package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import static jakarta.persistence.GenerationType.*;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoRepositorio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade de domínio representando um serviço oferecido por um profissional.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "SERVICO_OFERECIDO")
class ServicoOferecidoJpa {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "PROFISSIONAL_ID", referencedColumnName = "ID")
    private ProfissionalJpa profissional;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "PRECO", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "DESCRICAO", nullable = false, length = 500)
    private String descricao;

    @Column(name = "DURACAO_MINUTOS", nullable = false)
    private Integer duracaoMinutos;

    @ManyToOne
    @JoinColumn(name = "SERVICO_PRINCIPAL_ID", referencedColumnName = "ID")
    private ServicoOferecidoJpa servicoPrincipal;

    @Column(name = "INTERVALO_LIMPEZA_MINUTOS")
    private Integer intervaloLimpezaMinutos;

    @Column(name = "ATIVO", nullable = false)
    private boolean ativo;

    @Column(name = "MOTIVO_INATIVIDADE", length = 255)
    private String motivoInatividade;

    public ServicoOferecidoJpa(
            ProfissionalJpa profissional,
            String nome,
            BigDecimal preco,
            String descricao,
            Integer duracaoMinutos
        ) {
        setProfissional(profissional);
        setNome(nome);
        setPreco(preco);
        setDescricao(descricao);
        setDuracaoMinutos(duracaoMinutos);
        this.ativo = true;
        this.motivoInatividade = null;
    }
}

interface ServicoOferecidoJpaRepository extends JpaRepository<ServicoOferecidoJpa, Integer> {    
}

@Repository
class ServicoOferecidoJpaRepositorioImpl implements ServicoOferecidoRepositorio {

    @Autowired
    private ServicoOferecidoJpaRepository servicoOferecidoJpaRepository;
    
    @Autowired 
    JpaMapeador mapeador;

    @Override
    public List<ServicoOferecido> listarTodos() {
        return servicoOferecidoJpaRepository
        .findAll()
        .stream()
        .map(jpa -> mapeador.map(jpa, ServicoOferecido.class))
        .toList();
    }

    @Override
    public List<ServicoOferecido> buscarAddOnDoServicoPrincipal(ServicoOferecidoId servicoPrincipalId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ServicoOferecido buscarPorNome(String nome) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ServicoOferecido> buscarPorProfissional(ProfissionalId profissionalId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean estaQualificado(String nomeServico, String nomeProfissional) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void salvarAssociacao(String nomeServico, String nomeProfissional) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ServicoOferecido buscarPorId(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void remover(Integer id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ServicoOferecido salvar(ServicoOferecido entity) {
        // TODO Auto-generated method stub
        return null;
    }

    
}