package com.cesarschool.barbearia.dominio.principal.cliente.caixa;
//QUEM FOI QUE CRIOU ESSA CLASSE AQUI? ME DIZ EH RAFA
/**
 * Enum representando os meios de pagamento aceitos.
 */
public enum MeioPagamento {
    DEBITO("Débito"),
    CREDITO("Crédito"),
    PIX("PIX"),
    DINHEIRO("Dinheiro");

    private final String descricao;

    MeioPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
