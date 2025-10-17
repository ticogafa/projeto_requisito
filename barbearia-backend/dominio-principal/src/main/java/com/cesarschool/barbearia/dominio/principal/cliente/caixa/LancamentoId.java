package com.cesarschool.barbearia.dominio.principal.cliente.caixa;

import java.util.UUID;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;

public final class LancamentoId extends ValueObjectId<UUID> {

    public LancamentoId(UUID value) {
        super(value);
    }

    public static LancamentoId novo() {
        return new LancamentoId(UUID.randomUUID());
    }
}