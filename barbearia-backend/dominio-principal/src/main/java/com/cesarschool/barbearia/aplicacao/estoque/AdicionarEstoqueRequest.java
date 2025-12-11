package com.cesarschool.barbearia.aplicacao.estoque;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para adicionar estoque.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdicionarEstoqueRequest {
    private Integer quantidade;
    private String observacao;
    private String usuarioResponsavel;
}
