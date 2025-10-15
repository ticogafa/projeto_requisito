package com.cesarschool.barbearia.dominio.principal.cliente.caixa;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import java.util.List;
import java.util.Optional;

public interface LancamentoRepositorio {

    void salvar(Lancamento lancamento);

    Optional<Lancamento> buscarPorId(LancamentoId id);

    List<Lancamento> buscarTodos();

    List<Lancamento> buscarPendentesPorCliente(ClienteId clienteId);
}