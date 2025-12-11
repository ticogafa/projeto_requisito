package com.cesarschool.barbearia.aplicacao.estoque;

import java.time.LocalDateTime;

/**
 * DTO expandido para produto com informações adicionais.
 * Segue padrão de Spring Data projection extendendo o resumo básico.
 */
public interface ProdutoResumoExpandido extends ProdutoResumo {
    LocalDateTime getDataCadastro();
    Integer getTotalMovimentacoes();
    LocalDateTime getUltimaMovimentacao();
    Boolean getEstoqueBaixo();
}
