package com.cesarschool.barbearia_backend.common;

public interface Repositorio<T, ID> {
    T obter(ID id);
    void salvar(Profissional profissional);
    void deletar(ProfissionalId id);
    List<T> buscarTodos();
}
