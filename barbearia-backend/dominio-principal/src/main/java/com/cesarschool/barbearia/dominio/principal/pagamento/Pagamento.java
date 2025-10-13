package com.cesarschool.barbearia.dominio.principal.pagamento;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;

public final class Pagamento {

    private PagamentoId id;
    private MeioPagamento meioPagamento;

    public Pagamento(PagamentoId id, MeioPagamento meioPagamento) {
        setId(id);
        setMeioPagamento(meioPagamento);
    }

    public void setId(PagamentoId id) {
        Validacoes.validarObjetoObrigatorio(id, "Id do Pagamento");
        this.id = id;
    }

    public void setMeioPagamento(MeioPagamento meioPagamento) {
        Validacoes.validarObjetoObrigatorio(meioPagamento, "Meio de Pagamento");
        this.meioPagamento = meioPagamento;
    }

    public PagamentoId getId() { return id; }
    public MeioPagamento getMeioPagamento() { return meioPagamento; }

}