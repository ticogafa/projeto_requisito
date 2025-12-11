package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.aplicacao.estoque.MovimentacaoEstoqueResumo;
import com.cesarschool.barbearia.aplicacao.estoque.ProdutoRepositorioAplicacao;
import com.cesarschool.barbearia.aplicacao.estoque.ProdutoResumo;
import com.cesarschool.barbearia.aplicacao.estoque.ProdutoResumoExpandido;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoId;

/**
 * Implementação JPA do repositório de aplicação para produtos/estoque.
 * Usa queries JPQL para retornar projeções diretamente.
 * Seguindo padrão SGB-2025-01.
 */
@Repository
class ProdutoRepositorioAplicacaoImpl implements ProdutoRepositorioAplicacao {

    @Autowired
    private ProdutoResumoQueryRepository produtoResumoRepo;
    
    @Autowired
    private MovimentacaoEstoqueResumoQueryRepository movimentacaoResumoRepo;

    @Override
    public List<ProdutoResumo> pesquisarResumos() {
        return produtoResumoRepo.buscarResumos();
    }

    @Override
    public List<ProdutoResumo> pesquisarComEstoqueBaixo() {
        return produtoResumoRepo.buscarComEstoqueBaixo();
    }

    @Override
    public List<ProdutoResumoExpandido> pesquisarResumosExpandidos() {
        return produtoResumoRepo.buscarResumosExpandidos();
    }

    @Override
    public ProdutoResumo buscarResumoPorId(Integer id) {
        return produtoResumoRepo.buscarResumoPorId(id);
    }

    @Override
    public List<MovimentacaoEstoqueResumo> buscarMovimentacoesPorProduto(ProdutoId produtoId) {
        return movimentacaoResumoRepo.buscarPorProduto(produtoId.getValor());
    }
}

/**
 * Repository para consultas de resumos de produto.
 * Usa projeção de interface Spring Data JPA.
 */
@Repository
interface ProdutoResumoQueryRepository extends JpaRepository<ProdutoJpa, Integer> {
    
    @Query("""
        SELECT p.id as id,
               p.nome as nome,
               p.estoque as estoque,
               p.preco as preco,
               p.estoqueMinimo as estoqueMinimo
        FROM ProdutoJpa p
        ORDER BY p.nome
        """)
    List<ProdutoResumo> buscarResumos();
    
    @Query("""
        SELECT p.id as id,
               p.nome as nome,
               p.estoque as estoque,
               p.preco as preco,
               p.estoqueMinimo as estoqueMinimo
        FROM ProdutoJpa p
        WHERE p.estoque < p.estoqueMinimo
        ORDER BY p.nome
        """)
    List<ProdutoResumo> buscarComEstoqueBaixo();
    
    @Query("""
        SELECT p.id as id,
               p.nome as nome,
               p.estoque as estoque,
               p.preco as preco,
               p.estoqueMinimo as estoqueMinimo,
               CAST(NULL AS timestamp) as dataCadastro,
               (SELECT COUNT(m) FROM MovimentacaoEstoqueJpa m WHERE m.produto.id = p.id) as totalMovimentacoes,
               (SELECT MAX(m.dataHora) FROM MovimentacaoEstoqueJpa m WHERE m.produto.id = p.id) as ultimaMovimentacao,
               CASE WHEN p.estoque < p.estoqueMinimo THEN true ELSE false END as estoqueBaixo
        FROM ProdutoJpa p
        ORDER BY p.nome
        """)
    List<ProdutoResumoExpandido> buscarResumosExpandidos();
    
    @Query("""
        SELECT p.id as id,
               p.nome as nome,
               p.estoque as estoque,
               p.preco as preco,
               p.estoqueMinimo as estoqueMinimo
        FROM ProdutoJpa p
        WHERE p.id = :id
        """)
    ProdutoResumo buscarResumoPorId(@Param("id") Integer id);
}

/**
 * Repository para consultas de resumos de movimentação de estoque.
 * Usa projeção de interface Spring Data JPA.
 */
@Repository
interface MovimentacaoEstoqueResumoQueryRepository extends JpaRepository<MovimentacaoEstoqueJpa, Integer> {
    
    @Query("""
        SELECT m.id as id,
               m.produto.id as produtoId,
               m.nomeProduto as produtoNome,
               CAST(m.tipo AS string) as tipo,
               m.quantidade as quantidade,
               m.estoqueAnterior as estoqueAnterior,
               m.estoqueAtual as estoqueNovo,
               m.observacao as observacao,
               m.dataHora as dataHora,
               m.usuarioResponsavel as usuarioResponsavel
        FROM MovimentacaoEstoqueJpa m
        WHERE m.produto.id = :produtoId
        ORDER BY m.dataHora DESC
        """)
    List<MovimentacaoEstoqueResumo> buscarPorProduto(@Param("produtoId") Integer produtoId);
}
