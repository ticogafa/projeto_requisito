package com.cesarschool.barbearia.dominio.compartilhado.base;

import java.util.List;
import java.util.Optional;


public interface Repositorio<T, I> {
    T salvar(T entity);

    //retornar o tipo direto
    Optional<T> buscarPorId(I id);

    List<T> listarTodos();
    
    void remover(I id);

}
