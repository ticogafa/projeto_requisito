package com.cesarschool.barbearia.aplicacao.estoque;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para remover estoque.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoverEstoqueRequest {
    private Integer quantidade;
    private String observacao;
    private String usuarioResponsavel;
}
