package com.cesarschool.barbearia.dominio.compartilhado.base;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;

public abstract class Entity<T extends ValueObjectId<?>> {
    
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
    
}
