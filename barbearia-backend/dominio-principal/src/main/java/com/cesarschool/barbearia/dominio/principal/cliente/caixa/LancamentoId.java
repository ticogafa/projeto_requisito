package com.cesarschool.barbearia.dominio.principal.cliente.caixa;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class LancamentoId implements Serializable {//implementa Serializable para facilitar que essa classe funcione em diversos frameworks eh so um detalhe mesmo.

    private String value;

    public LancamentoId() {
        this.value = UUID.randomUUID().toString();
    }

    public LancamentoId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LancamentoId that = (LancamentoId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}