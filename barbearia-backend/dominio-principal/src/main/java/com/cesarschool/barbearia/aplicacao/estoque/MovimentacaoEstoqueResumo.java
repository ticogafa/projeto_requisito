package com.cesarschool.barbearia.aplicacao.estoque;

import java.time.LocalDateTime;

/**
 * DTO para representar resumo de movimentação de estoque.
 * Seguindo o padrão SGB de interface-based projection.
 */
public interface MovimentacaoEstoqueResumo {
    Integer getId();
    Integer getProdutoId();
    String getProdutoNome();
    String getTipo();
    Integer getQuantidade();
    Integer getEstoqueAnterior();
    Integer getEstoqueNovo();
    String getObservacao();
    LocalDateTime getDataHora();
    String getUsuarioResponsavel();
}
