package com.cesarschool.barbearia.dominio.compartilhado.observer;

public interface Sujeito<T> {
    void adicionarObservador(Observador<T> observador);
    void removerObservador(Observador<T> observador);
    void notificarObservadores(T evento);
}