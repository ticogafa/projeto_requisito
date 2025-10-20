package com.cesarschool.barbearia.dominio.principal.cliente.caixa;

import java.time.LocalDateTime;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;

import lombok.Getter;

@Getter
public class Lancamento {

    private final LancamentoId id;
    private final ClienteId clienteId; 
    private StatusLancamento status;
    private final String descricao;
    private final double valor;
    private final LocalDateTime quando;
    private final MeioPagamento meioPagamento;

    private Lancamento(LancamentoId id,
        ClienteId clienteId,
        StatusLancamento status,
        MeioPagamento meioPagamento,
        String descricao,
        double valor,
        LocalDateTime quando) {
            if (valor < 0) throw new IllegalArgumentException("Valor não pode ser negativo");
            this.id = Validacoes.requerirObjetoObrigatorio(id, "id");
            this.status = Validacoes.requerirObjetoObrigatorio(status, "status");
            this.descricao = Validacoes.requerirObjetoObrigatorio(descricao, "descricao");
            this.quando = Validacoes.requerirObjetoObrigatorio(quando, "quando");
            this.meioPagamento = Validacoes.requerirObjetoObrigatorio(meioPagamento, "meioPagamento");
            this.valor = valor;
            this.clienteId = clienteId;
    }

    public static Lancamento novoRecibemento(String descricao, double valor, MeioPagamento meioPagamento) {
        return new Lancamento(LancamentoId.novo(), null, StatusLancamento.ENTRADA, meioPagamento, descricao, valor, LocalDateTime.now());
    }

    public static Lancamento novoGasto(String descricao, double valor, MeioPagamento meioPagamento) {
        return new Lancamento(LancamentoId.novo(), null, StatusLancamento.SAIDA, meioPagamento, descricao, valor, LocalDateTime.now());
    }

    public static Lancamento novaDivida(ClienteId clienteId, String descricao, double valor, MeioPagamento meioPagamento) {
        return new Lancamento(LancamentoId.novo(), Validacoes.requerirObjetoObrigatorio(clienteId, "clienteId"), StatusLancamento.PENDENTE, meioPagamento, descricao, valor, LocalDateTime.now());
    }

    public void quitar() {
        if (this.status != StatusLancamento.PENDENTE) {
            throw new IllegalStateException("Apenas dívidas pendentes podem ser quitadas");
        }
        this.status = StatusLancamento.PAGO;
    }
}