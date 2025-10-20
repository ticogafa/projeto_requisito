package com.cesarschool.barbearia.dominio.principal.produto.estoque;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;

/**
 * Value Object que representa o identificador único de uma movimentação de estoque.
 * 
 * @author Sistema de Barbearia
 * @version 1.0
 */
public final class MovimentacaoEstoqueId extends ValueObjectId<Integer> {

    public MovimentacaoEstoqueId(Integer valor) {
        super(valor);
    }
}
