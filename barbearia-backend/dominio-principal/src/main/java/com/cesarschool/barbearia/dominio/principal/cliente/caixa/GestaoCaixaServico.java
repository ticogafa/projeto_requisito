package com.cesarschool.barbearia.dominio.principal.cliente.caixa;

import java.util.List;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;

public class GestaoCaixaServico {
    private final LancamentoRepositorio repositorio;

    public GestaoCaixaServico(LancamentoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    
    public void registrarEntrada(String descricao, double valor, MeioPagamento meio) {
        repositorio.salvar(Lancamento.novoRecibemento(descricao, valor, meio));
    }

    public void registrarSaida(String descricao, double valor, MeioPagamento meio) {
        repositorio.salvar(Lancamento.novoGasto(descricao, valor, meio));
    }

    public void registrarDivida(ClienteId clienteId, String descricao, double valor, MeioPagamento meio) {
        repositorio.salvar(Lancamento.novaDivida(clienteId, descricao, valor, meio));
    }

    
    public void registrarEntrada(String descricao, double valor) {
        registrarEntrada(descricao, valor, MeioPagamento.DINHEIRO);
    }

    public void registrarSaida(String descricao, double valor) {
        registrarSaida(descricao, valor, MeioPagamento.DINHEIRO);
    }

    public void registrarDivida(ClienteId clienteId, String descricao, double valor) {
        registrarDivida(clienteId, descricao, valor, MeioPagamento.DINHEIRO);
    }

    
    public void pagarPrimeiraDivida(ClienteId clienteId, double valorPago, MeioPagamento meioPagamento) {
        var pendentes = repositorio.buscarPendentesPorCliente(clienteId);
        if (pendentes.isEmpty()) throw new IllegalStateException("Nenhuma dívida pendente para o cliente");
        var divida = pendentes.get(0);
        divida.quitar();
        repositorio.salvar(divida);
        repositorio.salvar(Lancamento.novoRecibemento("Pagamento de dívida", valorPago, meioPagamento));
    }

    
    public void pagarPrimeiraDivida(ClienteId clienteId, double valorPago) {
        pagarPrimeiraDivida(clienteId, valorPago, MeioPagamento.DINHEIRO);
    }

    public double saldoAtual() {
        List<Lancamento> todos = repositorio.buscarTodos();
        double entradas = todos.stream().filter(l -> l.getStatus() == StatusLancamento.ENTRADA)
                .mapToDouble(Lancamento::getValor).sum();
        double saidas = todos.stream().filter(l -> l.getStatus() == StatusLancamento.SAIDA)
                .mapToDouble(Lancamento::getValor).sum();
        return entradas - saidas; 
    }
}