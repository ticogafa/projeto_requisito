package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.dominio.principal.produto.ProdutoId;
import com.cesarschool.barbearia.dominio.principal.produto.estoque.MovimentacaoEstoque;
import com.cesarschool.barbearia.dominio.principal.produto.estoque.MovimentacaoEstoqueRepositorio;
import com.cesarschool.barbearia.dominio.principal.produto.estoque.TipoMovimentacao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidade JPA representando uma Movimentação de Estoque no banco de dados.
 * Package-private seguindo o padrão do projeto.j
 */
@Entity
@Table(name = "MOVIMENTACAO_ESTOQUE")
class MovimentacaoEstoqueJpa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    Integer id;
    
    @ManyToOne
    @JoinColumn(name = "PRODUTO_ID", nullable = false)
    ProdutoJpa produto;
    
    @Column(name = "NOME_PRODUTO", nullable = false, length = 200)
    String nomeProduto;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO", nullable = false, length = 50)
    TipoMovimentacao tipo;
    
    @Column(name = "QUANTIDADE", nullable = false)
    int quantidade;
    
    @Column(name = "ESTOQUE_ANTERIOR", nullable = false)
    int estoqueAnterior;
    
    @Column(name = "ESTOQUE_ATUAL", nullable = false)
    int estoqueAtual;
    
    @Column(name = "DATA_HORA", nullable = false)
    LocalDateTime dataHora;
    
    @Column(name = "OBSERVACAO", length = 500)
    String observacao;
    
    @Column(name = "USUARIO_RESPONSAVEL", length = 100)
    String usuarioResponsavel;
    
    @Override
    public String toString() {
        return tipo.getDescricao() + " - " + nomeProduto;
    }
}

/**
 * Repositório Spring Data JPA para MovimentacaoEstoqueJpa.
 * Package-private seguindo o padrão do projeto.
 */
interface MovimentacaoEstoqueJpaRepository extends JpaRepository<MovimentacaoEstoqueJpa, Integer> {
    
    /**
     * Busca todas as movimentações de um produto específico.
     */
    @Query("SELECT m FROM MovimentacaoEstoqueJpa m WHERE m.produto.id = :produtoId ORDER BY m.dataHora DESC")
    List<MovimentacaoEstoqueJpa> findByProdutoId(@Param("produtoId") Integer produtoId);
    
    /**
     * Busca movimentações por tipo.
     */
    @Query("SELECT m FROM MovimentacaoEstoqueJpa m WHERE m.tipo = :tipo ORDER BY m.dataHora DESC")
    List<MovimentacaoEstoqueJpa> findByTipo(@Param("tipo") TipoMovimentacao tipo);
    
    /**
     * Busca movimentações em um período de datas.
     */
    @Query("SELECT m FROM MovimentacaoEstoqueJpa m WHERE m.dataHora BETWEEN :inicio AND :fim ORDER BY m.dataHora DESC")
    List<MovimentacaoEstoqueJpa> findByPeriodo(
        @Param("inicio") LocalDateTime inicio, 
        @Param("fim") LocalDateTime fim
    );
    
    /**
     * Busca movimentações de um produto em um período.
     */
    @Query("SELECT m FROM MovimentacaoEstoqueJpa m " +
           "WHERE m.produto.id = :produtoId " +
           "AND m.dataHora BETWEEN :inicio AND :fim " +
           "ORDER BY m.dataHora DESC")
    List<MovimentacaoEstoqueJpa> findByProdutoIdAndPeriodo(
        @Param("produtoId") Integer produtoId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );
    
    /**
     * Busca última movimentação de um produto.
     */
    @Query("SELECT m FROM MovimentacaoEstoqueJpa m " +
           "WHERE m.produto.id = :produtoId " +
           "ORDER BY m.dataHora DESC " +
           "LIMIT 1")
    MovimentacaoEstoqueJpa findUltimaMovimentacaoPorProduto(@Param("produtoId") Integer produtoId);
    
    /**
     * Busca as últimas N movimentações de um produto.
     */
    @Query("SELECT m FROM MovimentacaoEstoqueJpa m " +
           "WHERE m.produto.id = :produtoId " +
           "ORDER BY m.dataHora DESC " +
           "LIMIT :limite")
    List<MovimentacaoEstoqueJpa> findTopByProdutoId(
        @Param("produtoId") Integer produtoId, 
        @Param("limite") int limite
    );
}

/**
 * Implementação do repositório de domínio para MovimentacaoEstoque.
 * Realiza a conversão entre entidades JPA e entidades de domínio.
 */
@Repository
class MovimentacaoEstoqueRepositorioImpl implements MovimentacaoEstoqueRepositorio {
    
    @Autowired
    MovimentacaoEstoqueJpaRepository repositorio;
    
    @Autowired
    JpaMapeador mapeador;
    
    @Override
    public MovimentacaoEstoque salvar(MovimentacaoEstoque movimentacao) {
        var movimentacaoJpa = mapeador.map(movimentacao, MovimentacaoEstoqueJpa.class);
        var salva = repositorio.save(movimentacaoJpa);
        return mapeador.map(salva, MovimentacaoEstoque.class);
    }
    
    @Override
    public MovimentacaoEstoque buscarPorId(Integer id) {
        var movimentacaoJpa = repositorio.findById(id)
            .orElse(null);
        return movimentacaoJpa != null ? mapeador.map(movimentacaoJpa, MovimentacaoEstoque.class) : null;
    }
    
    @Override
    public List<MovimentacaoEstoque> listarTodos() {
        var movimentacoesJpa = repositorio.findAll();
        return movimentacoesJpa.stream()
            .map(mj -> mapeador.map(mj, MovimentacaoEstoque.class))
            .toList();
    }
    
    @Override
    public void remover(Integer id) {
        repositorio.deleteById(id);
    }
    
    @Override
    public List<MovimentacaoEstoque> buscarPorProduto(ProdutoId produtoId) {
        var movimentacoesJpa = repositorio.findByProdutoId(produtoId.getValor());
        return movimentacoesJpa.stream()
            .map(mj -> mapeador.map(mj, MovimentacaoEstoque.class))
            .toList();
    }
    
    @Override
    public List<MovimentacaoEstoque> buscarPorProdutoEPeriodo(
            ProdutoId produtoId, 
            LocalDateTime dataInicio, 
            LocalDateTime dataFim) {
        var movimentacoesJpa = repositorio.findByProdutoIdAndPeriodo(
            produtoId.getValor(), dataInicio, dataFim);
        return movimentacoesJpa.stream()
            .map(mj -> mapeador.map(mj, MovimentacaoEstoque.class))
            .toList();
    }
    
    @Override
    public List<MovimentacaoEstoque> buscarPorTipo(TipoMovimentacao tipo) {
        var movimentacoesJpa = repositorio.findByTipo(tipo);
        return movimentacoesJpa.stream()
            .map(mj -> mapeador.map(mj, MovimentacaoEstoque.class))
            .toList();
    }
    
    @Override
    public List<MovimentacaoEstoque> buscarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        var movimentacoesJpa = repositorio.findByPeriodo(dataInicio, dataFim);
        return movimentacoesJpa.stream()
            .map(mj -> mapeador.map(mj, MovimentacaoEstoque.class))
            .toList();
    }
    
    @Override
    public List<MovimentacaoEstoque> buscarUltimasMovimentacoes(ProdutoId produtoId, int limite) {
        var movimentacoesJpa = repositorio.findTopByProdutoId(produtoId.getValor(), limite);
        return movimentacoesJpa.stream()
            .map(mj -> mapeador.map(mj, MovimentacaoEstoque.class))
            .toList();
    }
}
