package com.cesarschool.barbearia.dominio.principal.profissional.avaliacao;

import java.util.UUID;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;

public final class AvaliacaoId extends ValueObjectId<String> {

    public AvaliacaoId(String id) {
        super(id);
    }

    // Método utilitário para gerar um novo ID aleatório
    public static AvaliacaoId novo() {
        return new AvaliacaoId(UUID.randomUUID().toString());
    }
}