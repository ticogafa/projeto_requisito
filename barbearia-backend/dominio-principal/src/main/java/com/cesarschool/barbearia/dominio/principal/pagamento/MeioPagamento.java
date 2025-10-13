package com.cesarschool.barbearia.dominio.principal.pagamento;

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
