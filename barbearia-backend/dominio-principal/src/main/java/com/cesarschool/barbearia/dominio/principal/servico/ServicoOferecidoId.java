package com.cesarschool.barbearia.dominio.principal.servico;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class ServicoOferecidoId extends ValueObjectId<Integer> {

    // APAGUE qualquer construtor vazio (public ServicoOferecidoId() {})
    
    // ESTA É A SOLUÇÃO:
    // O @JsonCreator avisa o Spring: "Use este construtor quando chegar um JSON"
    // O @JsonProperty("valor") liga o campo "valor" do JSON com o argumento aqui.
    @JsonCreator
    public ServicoOferecidoId(@JsonProperty("valor") Integer valor) {
        super(valor);
    }
}