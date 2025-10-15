package com.cesarschool.barbearia.dominio.principal.cliente.caixa;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;

public class Lancamento {

    private LancamentoId id;
    private ClienteId clienteId; 
    private StatusLancamento status;
    private String descricao;
    private double valor;

    // Construtor privado para forçar o uso dos factory methods
    private Lancamento(ClienteId clienteId, StatusLancamento status, String descricao, double valor) {
        this.id = new LancamentoId();
        this.clienteId = clienteId;
        this.status = status;
        this.descricao = descricao;
        this.valor = valor;
    }

    // Factory Methods para criação explícita
    public static Lancamento novaEntrada(String descricao, double valor) {
        return new Lancamento(null, StatusLancamento.ENTRADA, descricao, valor);
    }

    public static Lancamento novaSaida(String descricao, double valor) {
        return new Lancamento(null, StatusLancamento.SAIDA, descricao, valor);
    }

    public static Lancamento novaDivida(ClienteId clienteId, String descricao, double valor) {
        if (clienteId == null) {
            throw new IllegalArgumentException("ClienteId não pode ser nulo para uma dívida.");
        }
        return new Lancamento(clienteId, StatusLancamento.PENDENTE, descricao, valor);
    }

    // Método de negócio para quitar uma dívida
    public void quitar() {
        if (this.status != StatusLancamento.PENDENTE) {
            throw new IllegalStateException("Apenas um lançamento pendente pode ser quitado.");
        }
        this.status = StatusLancamento.PAGO;
    }


    // Getters
    public LancamentoId getId() {
        return id;
    }

    public ClienteId getClienteId() {
        return clienteId;
    }

    public StatusLancamento getStatus() {
        return status;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    // O setter público foi removido para proteger o estado da entidade.
}