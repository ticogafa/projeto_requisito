package com.cesarschool.barbearia.dominio.profissionais.servico;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;

/**
 * Value Object representando o identificador único de um Serviço Oferecido.
 */
public final class ServicoOferecidoId extends ValueObjectId<Integer> {

    public ServicoOferecidoId(Integer valor) {
        super(valor);
    }


}
