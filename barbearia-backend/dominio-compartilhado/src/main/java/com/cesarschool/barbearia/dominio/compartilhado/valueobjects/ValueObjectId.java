package com.cesarschool.barbearia.dominio.compartilhado.valueobjects;

import java.util.Objects;

import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarInteiroPositivo;
import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarObjetoObrigatorio;

/**
 * Classe base abstrata para Value Objects de ID.
 * Fornece implementação comum de equals, hashCode e toString.
 */
public abstract class ValueObjectId<T> {
    
    private final T valor;
    
    protected ValueObjectId(T valor) {
        validarObjetoObrigatorio(valor, "Valor do ID");
        if(valor instanceof Integer integer) validarInteiroPositivo(integer, "ID");
        this.valor = valor;
    }
    
    public T getValor() {
        return valor;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueObjectId<?> that = (ValueObjectId<?>) o;
        return Objects.equals(valor, that.valor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + valor + ")";
    }
}
