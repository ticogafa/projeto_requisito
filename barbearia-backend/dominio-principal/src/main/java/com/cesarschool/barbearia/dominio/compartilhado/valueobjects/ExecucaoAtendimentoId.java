package com.cesarschool.barbearia.dominio.compartilhado.valueobjects;

import java.util.UUID;

public final class ExecucaoAtendimentoId extends ValueObjectId<UUID> {
    public ExecucaoAtendimentoId(UUID value) { super(value); }
    public static ExecucaoAtendimentoId novo() { return new ExecucaoAtendimentoId(UUID.randomUUID()); }
}