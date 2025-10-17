package com.cesarschool.barbearia.dominio.principal.cliente.caixa;

import java.util.List;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;

public class GestaoCaixaServico {
    private final LancamentoRepositorio repositorio;

    public GestaoCaixaServico(LancamentoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public void registrarEntrada(String descricao, double valor) {
        repositorio.salvar(Lancamento.novaEntrada(descricao, valor));
    }

    public void registrarSaida(String descricao, double valor) {
        repositorio.salvar(Lancamento.novaSaida(descricao, valor));
    }

    public void registrarDivida(ClienteId clienteId, String descricao, double valor) {
        repositorio.salvar(Lancamento.novaDivida(clienteId, descricao, valor));
    }

    public void pagarPrimeiraDivida(ClienteId clienteId, double valorPago) {
        var pendentes = repositorio.buscarPendentesPorCliente(clienteId);
        if (pendentes.isEmpty()) throw new IllegalStateException("Nenhuma dívida pendente para o cliente");
        var divida = pendentes.get(0);
        divida.quitar();
        repositorio.salvar(divida);
        repositorio.salvar(Lancamento.novaEntrada("Pagamento de dívida", valorPago));
    }

    public double saldoAtual() {
        List<Lancamento> todos = repositorio.buscarTodos();
        double entradas = todos.stream().filter(l -> l.getStatus() == StatusLancamento.ENTRADA)
                .mapToDouble(Lancamento::getValor).sum();
        double saidas = todos.stream().filter(l -> l.getStatus() == StatusLancamento.SAIDA)
                .mapToDouble(Lancamento::getValor).sum();
        return entradas - saidas; // pendentes não afetam saldo
    }
}