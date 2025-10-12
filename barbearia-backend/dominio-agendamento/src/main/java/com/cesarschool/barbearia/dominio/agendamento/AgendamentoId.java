package com.cesarschool.barbearia.dominio.agendamento;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;

/**
 * Value Object representando o identificador único de um Agendamento.
 */
public final class AgendamentoId extends ValueObjectId<Integer> {

    public AgendamentoId(Integer valor) {
        super(valor);
    }

}
