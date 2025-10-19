package com.cesarschool.barbearia.dominio.principal.cliente.caixa;

import java.time.LocalDateTime;
import java.util.Objects;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;

public class Lancamento {

    private final LancamentoId id;
    private final ClienteId clienteId; 
    private StatusLancamento status;
    private final String descricao;
    private final double valor;
    private final LocalDateTime quando;

    private Lancamento(LancamentoId id,
                       ClienteId clienteId,
                       StatusLancamento status,
                       MeioPagamento meioPagamento,
                       String descricao,
                       double valor,
                       LocalDateTime quando) {
        if (valor < 0) throw new IllegalArgumentException("Valor não pode ser negativo");
        this.id = Objects.requireNonNull(id, "id");
        this.status = Objects.requireNonNull(status, "status");
        this.descricao = Objects.requireNonNull(descricao, "descricao");
        this.quando = Objects.requireNonNull(quando, "quando");
        this.valor = valor;
        this.clienteId = clienteId;
    }

    public static Lancamento novaEntrada(String descricao, double valor, MeioPagamento meioPagamento) {
        return new Lancamento(LancamentoId.novo(), null, StatusLancamento.ENTRADA, meioPagamento, descricao, valor, LocalDateTime.now());
    }

    public static Lancamento novaSaida(String descricao, double valor, MeioPagamento meioPagamento) {
        return new Lancamento(LancamentoId.novo(), null, StatusLancamento.SAIDA, meioPagamento, descricao, valor, LocalDateTime.now());
    }

    public static Lancamento novaDivida(ClienteId clienteId, String descricao, double valor, MeioPagamento meioPagamento) {
        return new Lancamento(LancamentoId.novo(), Objects.requireNonNull(clienteId), StatusLancamento.PENDENTE, meioPagamento, descricao, valor, LocalDateTime.now());
    }

    public void quitar() {
        if (this.status != StatusLancamento.PENDENTE) {
            throw new IllegalStateException("Apenas dívidas pendentes podem ser quitadas");
        }
        this.status = StatusLancamento.PAGO;
    }

    public LancamentoId getId() { return id; }
    public ClienteId getClienteId() { return clienteId; }
    public StatusLancamento getStatus() { return status; }
    public String getDescricao() { return descricao; }
    public double getValor() { return valor; }
    public LocalDateTime getQuando() { return quando; }
}