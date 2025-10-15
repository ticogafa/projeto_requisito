package com.cesarschool.barbearia.dominio.principal.cliente.caixa;

import java.util.UUID;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;

public class LancamentoId extends ValueObjectId<String> {

    public LancamentoId() {
        super(UUID.randomUUID().toString()); //faz um identificador único automático
    }

    public LancamentoId(String value) {
        super(value);
    }
}