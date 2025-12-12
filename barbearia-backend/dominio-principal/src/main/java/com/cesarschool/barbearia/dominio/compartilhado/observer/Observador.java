package com.cesarschool.barbearia.dominio.compartilhado.observer;

public interface Observador<T> {
    void atualizar(T evento);
}