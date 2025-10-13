package com.cesarschool.barbearia.dominio.principal.horariotrabalho;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;

/**
 * Value Object representando o identificador único de um Horário de Trabalho.
 */
public final class HorarioTrabalhoId extends ValueObjectId<Integer> {

    public HorarioTrabalhoId(Integer valor) {
        super(valor);
    }
}
