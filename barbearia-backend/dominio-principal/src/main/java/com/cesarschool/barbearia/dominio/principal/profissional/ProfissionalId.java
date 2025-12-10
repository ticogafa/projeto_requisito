package com.cesarschool.barbearia.dominio.principal.profissional;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;

/**
 * Value Object representando o identificador único de um Profissional.
 * Imutável e com validação.
 */
// CORREÇÃO: Alterado de <Integer> para <String>
public final class ProfissionalId extends ValueObjectId<String> {

    public ProfissionalId(String profissionalId) {
        super(profissionalId);
    }

}
