package com.cesarschool.barbearia.aplicacao.estoque;

import java.math.BigDecimal;

/**
 * DTO para representar resumo básico de produto.
 * Seguindo o padrão SGB de interface-based projection.
 */
public interface ProdutoResumo {
    Integer getId();
    String getNome();
    Integer getEstoque();
    BigDecimal getPreco();
    Integer getEstoqueMinimo();
}
