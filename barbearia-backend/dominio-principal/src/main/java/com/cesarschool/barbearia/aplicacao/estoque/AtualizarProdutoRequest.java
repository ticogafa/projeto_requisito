package com.cesarschool.barbearia.aplicacao.estoque;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para atualização de produto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarProdutoRequest {
    private String nome;
    private Integer estoque;
    private BigDecimal preco;
    private Integer estoqueMinimo;
    private String usuarioResponsavel;
}
