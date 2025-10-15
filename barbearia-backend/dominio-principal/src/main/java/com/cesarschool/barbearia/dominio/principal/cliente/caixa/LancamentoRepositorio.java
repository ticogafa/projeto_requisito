package com.cesarschool.barbearia.dominio.principal.cliente.caixa;

import java.util.List;
import java.util.Optional;

public interface LancamentoRepositorio {

    void salvar(Lancamento lancamento);

    Optional<Lancamento> buscarPorId(LancamentoId id);

    List<Lancamento> buscarTodos();
    
    // Outros métodos de busca podem ser adicionados aqui conforme a necessidade.
    // Ex: List<Lancamento> buscarPorStatus(String status);
}