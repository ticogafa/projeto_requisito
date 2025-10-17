package com.cesarschool.cucumber.agendamento.infraestrutura;

import java.time.LocalDateTime;

import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

public class AgendamentoConflitoRepositorio extends AgendamentoMockRepositorio {
    @Override
    public boolean existeAgendamentoNoPeriodo(ProfissionalId profissionalId, LocalDateTime dataHora,
            int duracaoMinutos) {
        return true;
    }

    @Override
    public Agendamento salvar(Agendamento agendamento) {
        return null;
    }
}