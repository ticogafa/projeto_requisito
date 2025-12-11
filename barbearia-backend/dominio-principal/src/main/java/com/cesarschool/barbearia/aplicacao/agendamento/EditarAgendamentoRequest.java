package com.cesarschool.barbearia.aplicacao.agendamento;

import java.time.LocalDateTime;

import com.cesarschool.barbearia.dominio.principal.agendamento.StatusAgendamento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para editar um agendamento.
 * Permite alterar data/hora, profissional, observações e status.
 * Não permite alterar serviço (requer cancelamento + novo agendamento).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditarAgendamentoRequest {
    private LocalDateTime dataHora;
    private Integer profissionalId;
    private String observacoes;
    private StatusAgendamento status;
}
