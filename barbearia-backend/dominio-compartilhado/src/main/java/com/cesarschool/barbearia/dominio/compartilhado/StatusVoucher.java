package com.cesarschool.barbearia.dominio.compartilhado;

/**
 * Enum representando os status possíveis de um voucher.
 */
public enum StatusVoucher {
    GERADO("Gerado"),
    UTILIZADO("Utilizado"),
    EXPIRADO("Expirado");

    private final String descricao;

    StatusVoucher(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean podeSerUtilizado() {
        return this == GERADO;
    }
}
