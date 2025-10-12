package com.cesarschool.barbearia.dominio.profissionais.profissional;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;

/**
 * Value Object representando o identificador único de um Profissional.
 * Imutável e com validação.
 */
public final class ProfissionalId extends ValueObjectId<Integer> {

    public ProfissionalId(Integer valor) {
        super(valor);
    }


}
