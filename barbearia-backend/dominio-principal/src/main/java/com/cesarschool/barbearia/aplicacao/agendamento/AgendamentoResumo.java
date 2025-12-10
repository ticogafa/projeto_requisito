package com.cesarschool.barbearia.aplicacao.agendamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para representar resumo de agendamento.
 * Seguindo o padrão SGB de interface-based projection.
 */
public interface AgendamentoResumo {
    Integer getId();
    LocalDateTime getDataHora();
    Integer getProfissionalId();
    String getProfissionalNome();
    Integer getClienteId();
    String getClienteNome();
    Integer getServicoId();
    String getServicoNome();
    BigDecimal getServicoPreco();
    String getStatus();
    String getObservacoes();
    String getClienteNome();
}
