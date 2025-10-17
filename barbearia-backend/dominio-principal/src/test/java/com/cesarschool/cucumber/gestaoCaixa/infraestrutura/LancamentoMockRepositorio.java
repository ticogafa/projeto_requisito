package com.cesarschool.cucumber.gestaoCaixa.infraestrutura;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.Lancamento;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.LancamentoId;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.LancamentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.StatusLancamento;

public class LancamentoMockRepositorio implements LancamentoRepositorio {
    private final Map<LancamentoId, Lancamento> store = new LinkedHashMap<>();

    @Override
    public void salvar(Lancamento lancamento) {
        store.put(lancamento.getId(), lancamento);
    }

    @Override
    public Optional<Lancamento> buscarPorId(LancamentoId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Lancamento> buscarTodos() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Lancamento> buscarPendentesPorCliente(ClienteId clienteId) {
        return store.values().stream()
                .filter(l -> l.getStatus() == StatusLancamento.PENDENTE && clienteId.equals(l.getClienteId()))
                .collect(Collectors.toList());
    }

    public void limpar() { store.clear(); }
}