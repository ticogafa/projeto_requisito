package com.cesarschool.barbearia.dominio.principal.itemvenda;

/**
 * Enum representando os tipos de venda.
 */
public enum TipoVenda {
    PRODUTO("Venda de Produto"),
    SERVICO("Venda de Serviço"),
    MISTO("Produto e Serviço");

    private final String descricao;

    TipoVenda(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
