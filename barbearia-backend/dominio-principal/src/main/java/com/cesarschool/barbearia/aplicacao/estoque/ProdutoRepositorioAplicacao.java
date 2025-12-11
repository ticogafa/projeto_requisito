package com.cesarschool.barbearia.aplicacao.estoque;

import java.util.List;

import com.cesarschool.barbearia.dominio.principal.produto.ProdutoId;

/**
 * Repositório da camada de aplicação para consultas de produtos/estoque.
 * Retorna DTOs/projeções ao invés de entidades de domínio.
 * Seguindo padrão SGB-2025-01.
 */
public interface ProdutoRepositorioAplicacao {
    
    /**
     * Lista todos os produtos em formato resumido.
     */
    List<ProdutoResumo> pesquisarResumos();
    
    /**
     * Lista produtos com estoque abaixo do mínimo.
     */
    List<ProdutoResumo> pesquisarComEstoqueBaixo();
    
    /**
     * Lista produtos em formato expandido com informações adicionais.
     */
    List<ProdutoResumoExpandido> pesquisarResumosExpandidos();
    
    /**
     * Busca um produto específico por ID.
     */
    ProdutoResumo buscarResumoPorId(Integer id);
    
    /**
     * Lista movimentações de estoque de um produto.
     */
    List<MovimentacaoEstoqueResumo> buscarMovimentacoesPorProduto(ProdutoId produtoId);
}
