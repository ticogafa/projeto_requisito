package com.cesarschool.barbearia.dominio.marketing.cliente;

import com.cesarschool.barbearia.dominio.compartilhado.ValueObjectId;

/**
 * Value Object representando o identificador único de um Cliente.
 */
public final class ClienteId extends ValueObjectId<Integer> {

    public ClienteId(Integer valor) {
        super(valor);
    }

}
