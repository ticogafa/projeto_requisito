package com.cesarschool.barbearia.dominio.principal.cliente;

import java.util.Objects;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;

/**
 * Value Object representando o identificador único de um Cliente.
 */
public final class ClienteId extends ValueObjectId<Integer> {
    public ClienteId(Integer valor) {
        super(valor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClienteId clienteId = (ClienteId) o;
        return Objects.equals(getValor(), clienteId.getValor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValor());
    }
}
