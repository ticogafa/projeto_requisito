package com.cesarschool.barbearia.dominio.principal.profissional;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;

public final class ProfissionalId extends ValueObjectId<Integer> {

    public ProfissionalId(Integer profissionalId) {
        super(profissionalId);
    }

}
