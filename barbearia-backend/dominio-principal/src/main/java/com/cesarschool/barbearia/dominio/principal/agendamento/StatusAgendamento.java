package com.cesarschool.barbearia.dominio.principal.agendamento;

/**
 * Enum representando os status possíveis de um agendamento.
 */
public enum StatusAgendamento {
    PENDENTE("Pendente"),
    CONFIRMADO("Confirmado"),
    EM_ANDAMENTO("Em Andamento"),
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
        return this.equals(PENDENTE) || this.equals(CONFIRMADO) || this.equals(EM_ANDAMENTO);
    }

    public boolean podeConfirmar() {
        return this.equals(StatusAgendamento.PENDENTE);
    }
}
