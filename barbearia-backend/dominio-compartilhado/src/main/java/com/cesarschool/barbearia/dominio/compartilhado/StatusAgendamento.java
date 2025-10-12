package com.cesarschool.barbearia.dominio.compartilhado;

/**
 * Enum representando os status possíveis de um agendamento.
 */
public enum StatusAgendamento {
    PENDENTE("Pendente"),
    CONFIRMADO("Confirmado"),
    CANCELADO("Cancelado"),
    CONCLUIDO("Concluído"),
    EM_ATENDIMENTO("Em Atendimento");

    private final String descricao;

    StatusAgendamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean podeSerCancelado() {
        return this == PENDENTE || this == CONFIRMADO;
    }

    public boolean podeIniciarAtendimento() {
        return this == CONFIRMADO;
    }
}
