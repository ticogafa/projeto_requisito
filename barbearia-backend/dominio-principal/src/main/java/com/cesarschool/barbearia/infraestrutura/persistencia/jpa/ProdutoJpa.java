package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.dominio.principal.produto.Produto;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoId;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoRepositorio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade JPA representando um Produto no banco de dados.
 * Package-private seguindo o padrão do projeto.
 */
@Entity
@Table(name = "PRODUTO")
class ProdutoJpa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    Integer id;
    
    @Column(name = "NOME", nullable = false, unique = true, length = 200)
    String nome;
    
    @Column(name = "ESTOQUE", nullable = false)
    int estoque;
    
    @Column(name = "PRECO", nullable = false, precision = 10, scale = 2)
    BigDecimal preco;
    
    @Column(name = "ESTOQUE_MINIMO", nullable = false)
    int estoqueMinimo;
    
    @Override
    public String toString() {
        return nome;
    }
}

/**
 * Repositório Spring Data JPA para ProdutoJpa.
 * Package-private seguindo o padrão do projeto.
 */
interface ProdutoJpaRepository extends JpaRepository<ProdutoJpa, Integer> {
    
    /**
     * Busca produto por nome (case-insensitive).
     */
    @Query("SELECT p FROM ProdutoJpa p WHERE LOWER(p.nome) = LOWER(:nome)")
    ProdutoJpa findByNomeIgnoreCase(String nome);
    
    /**
     * Verifica se existe produto com o nome informado.
     */
    @Query("SELECT COUNT(p) > 0 FROM ProdutoJpa p WHERE LOWER(p.nome) = LOWER(:nome)")
    boolean existsByNomeIgnoreCase(String nome);
    
    /**
     * Verifica se existe produto com o nome informado, excluindo um ID específico.
     */
    @Query("SELECT COUNT(p) > 0 FROM ProdutoJpa p WHERE LOWER(p.nome) = LOWER(:nome) AND p.id != :id")
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Integer id);
    
    /**
     * Busca produtos com estoque abaixo do mínimo.
     */
    @Query("SELECT p FROM ProdutoJpa p WHERE p.estoque < p.estoqueMinimo ORDER BY p.nome")
    List<ProdutoJpa> findProdutosAbaixoEstoqueMinimo();
}

/**
 * Implementação do repositório de domínio para Produto.
 * Realiza a conversão entre entidades JPA e entidades de domínio.
 */
@Repository
class ProdutoRepositorioImpl implements ProdutoRepositorio {
    
    @Autowired
    ProdutoJpaRepository repositorio;
    
    @Autowired
    JpaMapeador mapeador;
    
    @Override
    public Produto salvar(Produto produto) {
        var produtoJpa = mapeador.map(produto, ProdutoJpa.class);
        var salvo = repositorio.save(produtoJpa);
        return mapeador.map(salvo, Produto.class);
    }
    
    @Override
    public Produto buscarPorId(Integer id) {
        var produtoJpa = repositorio.findById(id)
            .orElse(null);
        return produtoJpa != null ? mapeador.map(produtoJpa, Produto.class) : null;
    }
    
    @Override
    public List<Produto> listarTodos() {
        var produtosJpa = repositorio.findAll();
        return produtosJpa.stream()
            .map(pj -> mapeador.map(pj, Produto.class))
            .toList();
    }
    
    @Override
    public void remover(Integer id) {
        repositorio.deleteById(id);
    }
    
    @Override
    public List<Produto> findProdutosComEstoqueBaixo() {
        var produtosJpa = repositorio.findProdutosAbaixoEstoqueMinimo();
        return produtosJpa.stream()
            .map(pj -> mapeador.map(pj, Produto.class))
            .toList();
    }
    
    @Override
    public List<Produto> listarProdutosComEstoqueBaixo() {
        return findProdutosComEstoqueBaixo();
    }
}
