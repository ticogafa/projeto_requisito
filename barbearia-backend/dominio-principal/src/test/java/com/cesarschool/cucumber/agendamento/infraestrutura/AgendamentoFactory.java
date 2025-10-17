package com.cesarschool.cucumber.agendamento.infraestrutura;

import java.time.LocalDateTime;

import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoId;
import com.cesarschool.barbearia.dominio.principal.agendamento.StatusAgendamento;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

public final class AgendamentoFactory {

    public static Agendamento criarPadrao() {

        return Agendamento.builder()
            .dataHora(LocalDateTime.now().plusHours(2))
            .clienteId(new ClienteId(1))
            .profissionalId(new ProfissionalId(1))
            .servicoId(new ServicoOferecidoId(1))
            .observacoes("Agendamento padrão para testes")
            .build();
    }

    public static Agendamento criarParaHorario(LocalDateTime dataHora) {
        Agendamento agendamento = criarPadrao();
        agendamento.setDataHora(dataHora);
        return agendamento;
    }

    public static Agendamento criarComStatus(StatusAgendamento status) {
        // Cria agendamento para menos de 2 horas no futuro
        LocalDateTime horarioProximo = LocalDateTime.now().plusMinutes(90);
        Agendamento agendamento = criarParaHorario(horarioProximo);
        agendamento.setStatus(status);
        agendamento.setId(new AgendamentoId(1)); // Simula um agendamento já salvo
        return agendamento;
    }

    private AgendamentoFactory() {}
}