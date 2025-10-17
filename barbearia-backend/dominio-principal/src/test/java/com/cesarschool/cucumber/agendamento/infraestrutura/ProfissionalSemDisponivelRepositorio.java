package com.cesarschool.cucumber.agendamento.infraestrutura;

import java.time.LocalDateTime;

import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;

public class ProfissionalSemDisponivelRepositorio extends ProfissionalMockRepositorio {
    @Override
    public Profissional buscarPrimeiroProfissionalDisponivel(LocalDateTime dataHora, int duracaoMinutos) {
        throw new IllegalStateException("Não há profissionais disponíveis para o horário solicitado");
    }
}