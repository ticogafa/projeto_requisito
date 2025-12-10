package com.cesarschool.barbearia.dominio.principal.servico;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class ServicoOferecidoId extends ValueObjectId<Integer> {
    @JsonCreator
    public ServicoOferecidoId(@JsonProperty("valor") Integer valor) {
        super(valor);
    }
}