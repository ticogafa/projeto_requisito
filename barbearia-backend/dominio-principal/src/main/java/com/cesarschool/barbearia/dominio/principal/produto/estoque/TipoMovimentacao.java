package com.cesarschool.barbearia.dominio.principal.produto.estoque;

/**
 * Enumeração que representa os tipos de movimentação de estoque.
 * 
 * @author Sistema de Barbearia
 * @version 1.0
 */
public enum TipoMovimentacao {
    /**
     * Entrada de produtos no estoque (compra, devolução, ajuste positivo)
     */
    ENTRADA("Entrada"),
    
    /**
     * Saída de produtos do estoque (venda, perda, ajuste negativo)
     */
    SAIDA("Saída"),
    
    /**
     * Venda realizada no PDV (Ponto de Venda)
     */
    VENDA("Venda PDV"),
    
    /**
     * Ajuste manual de estoque
     */
    AJUSTE("Ajuste"),
    
    /**
     * Estoque inicial do produto
     */
    ESTOQUE_INICIAL("Estoque Inicial"),
    
    /**
     * Desativação do produto
     */
    DESATIVACAO("Desativação");

    private final String descricao;

    TipoMovimentacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
