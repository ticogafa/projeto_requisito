package com.cesarschool.barbearia.dominio.principal.cliente.caixa;

import java.math.BigDecimal;
import java.util.List;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;

public class GestaoCaixaServico {
    private final LancamentoRepositorio repositorio;

    public GestaoCaixaServico(LancamentoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public void registrarEntrada(String descricao, BigDecimal valor, MeioPagamento meio) {
        repositorio.salvar(Lancamento.novoRecibemento(descricao, valor, meio));
    }

    public void registrarSaida(String descricao, BigDecimal valor, MeioPagamento meio) {
        repositorio.salvar(Lancamento.novoGasto(descricao, valor, meio));
    }

    public void registrarDivida(ClienteId clienteId, String descricao, BigDecimal valor, MeioPagamento meio) {
        repositorio.salvar(Lancamento.novaDivida(clienteId, descricao, valor, meio));
    }

    public void registrarEntrada(String descricao, BigDecimal valor) {
        registrarEntrada(descricao, valor, MeioPagamento.DINHEIRO);
    }

    public void registrarSaida(String descricao, BigDecimal valor) {
        registrarSaida(descricao, valor, MeioPagamento.DINHEIRO);
    }

    public void registrarDivida(ClienteId clienteId, String descricao, BigDecimal valor) {
        registrarDivida(clienteId, descricao, valor, MeioPagamento.DINHEIRO);
    }

    public void pagarPrimeiraDivida(ClienteId clienteId, BigDecimal valorPago, MeioPagamento meioPagamento) {
        var pendentes = repositorio.buscarPendentesPorCliente(clienteId);
        if (pendentes.isEmpty()) throw new IllegalStateException("Nenhuma dívida pendente para o cliente");
        var divida = pendentes.get(0);
        divida.quitar();
        repositorio.salvar(divida);
        repositorio.salvar(Lancamento.novoRecibemento("Pagamento de dívida", valorPago, meioPagamento));
    }

    public void pagarPrimeiraDivida(ClienteId clienteId, BigDecimal valorPago) {
        pagarPrimeiraDivida(clienteId, valorPago, MeioPagamento.DINHEIRO);
    }

    public BigDecimal saldoAtual() {
        List<Lancamento> todos = repositorio.buscarTodos();
        BigDecimal entradas = todos.stream()
                
                .filter(l -> l.getStatus() == StatusLancamento.ENTRADA)
                .map(Lancamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal saidas = todos.stream()
                .filter(l -> l.getStatus() == StatusLancamento.SAIDA)
                .map(Lancamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return entradas.subtract(saidas);
    }
}