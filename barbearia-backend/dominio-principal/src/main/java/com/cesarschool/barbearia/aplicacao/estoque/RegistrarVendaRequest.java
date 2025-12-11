package com.cesarschool.barbearia.aplicacao.estoque;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para registrar venda de produto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarVendaRequest {
    private Integer quantidade;
    private String observacao;
    private String usuarioResponsavel;
}
