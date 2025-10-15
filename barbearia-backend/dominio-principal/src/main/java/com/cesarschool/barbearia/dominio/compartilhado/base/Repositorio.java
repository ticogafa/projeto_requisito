package com.cesarschool.barbearia.dominio.compartilhado.base;

import java.util.List;

public interface Repositorio<T, I> {
    T salvar(T entity);

    T buscarPorId(I id);

    List<T> listarTodos();
    
    void remover(I id);

}
