package com.cesarschool.barbearia.dominio.principal.produto.estoque;

import java.time.LocalDateTime;
import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.base.Repositorio;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoId;

/**
 * Interface do repositório para persistência de movimentações de estoque.
 * Estende o repositório base com operações específicas de consulta de histórico.
 * 
 * @author Sistema de Barbearia
 * @version 1.0
 */
public interface MovimentacaoEstoqueRepositorio extends Repositorio<MovimentacaoEstoque, Integer> {

    /**
     * Busca todas as movimentações de um produto específico.
     * 
     * @param produtoId ID do produto
     * @return Lista de movimentações do produto ordenada por data (mais recente primeiro)
     */
    List<MovimentacaoEstoque> buscarPorProduto(ProdutoId produtoId);

    /**
     * Busca movimentações de um produto em um período específico.
     * 
     * @param produtoId ID do produto
     * @param dataInicio Data inicial do período
     * @param dataFim Data final do período
     * @return Lista de movimentações no período
     */
    List<MovimentacaoEstoque> buscarPorProdutoEPeriodo(
        ProdutoId produtoId, 
        LocalDateTime dataInicio, 
        LocalDateTime dataFim
    );

    /**
     * Busca movimentações por tipo.
     * 
     * @param tipo Tipo de movimentação (ENTRADA, SAIDA, VENDA, etc.)
     * @return Lista de movimentações do tipo especificado
     */
    List<MovimentacaoEstoque> buscarPorTipo(TipoMovimentacao tipo);

    /**
     * Busca todas as movimentações em um período específico.
     * 
     * @param dataInicio Data inicial do período
     * @param dataFim Data final do período
     * @return Lista de movimentações no período
     */
    List<MovimentacaoEstoque> buscarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim);

    /**
     * Busca as últimas N movimentações de um produto.
     * 
     * @param produtoId ID do produto
     * @param limite Quantidade máxima de registros
     * @return Lista com as últimas movimentações
     */
    List<MovimentacaoEstoque> buscarUltimasMovimentacoes(ProdutoId produtoId, int limite);
}
