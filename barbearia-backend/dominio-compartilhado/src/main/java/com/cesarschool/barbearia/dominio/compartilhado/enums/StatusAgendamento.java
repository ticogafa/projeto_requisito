package com.cesarschool.barbearia.dominio.compartilhado.enums;

/**
 * Enum representando os status possíveis de um agendamento.
 */
public enum StatusAgendamento {
    PENDENTE("Pendente"),
    CONFIRMADO("Confirmado"),
    CANCELADO("Cancelado"),
    CONCLUIDO("Concluído");

    private final String descricao;

    StatusAgendamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean podeSerCancelado() {
        return this.equals(PENDENTE) || this.equals(CONFIRMADO);
    }

    public boolean podeConfirmar() {
        return this.equals(StatusAgendamento.PENDENTE);
    }
}
