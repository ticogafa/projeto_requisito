package com.cesarschool.barbearia.aplicacao.agendamento;

/**
 * DTO para representar profissional disponível para agendamento.
 * Seguindo o padrão SGB de interface-based projection.
 */
public interface ProfissionalDisponivelResumo {
    Integer getId();
    String getNome();
    String getSenioridade();
}
