package com.cesarschool.barbearia.aplicacao.estoque;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para cadastro de novo produto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarProdutoRequest {
    private String nome;
    private BigDecimal preco;
    private Integer estoqueInicial;
    private Integer estoqueMinimo;
    private String usuarioResponsavel;
}
