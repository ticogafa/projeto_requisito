package com.cesarschool.barbearia.aplicacao.agendamento;

import java.time.LocalDateTime;

/**
 * DTO para representar resumo de agendamento.
 * Seguindo o padrão SGB de interface-based projection.
 */
public interface AgendamentoResumo {
    Integer getId();
    LocalDateTime getDataHora();
    String getProfissionalNome();
    String getServicoNome();
    String getStatus();
    String getObservacoes();
}
