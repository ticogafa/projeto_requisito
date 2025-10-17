package com.cesarschool.barbearia.dominio.principal.cliente.caixa;

import java.util.List;
import java.util.Optional;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;

public interface LancamentoRepositorio {
    void salvar(Lancamento lancamento);
    Optional<Lancamento> buscarPorId(LancamentoId id);
    List<Lancamento> buscarTodos();
    List<Lancamento> buscarPendentesPorCliente(ClienteId clienteId);
}