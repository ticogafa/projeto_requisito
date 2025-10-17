package com.cesarschool.barbearia.dominio.principal.profissional.atendimento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ExecucaoAtendimentoId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

public interface ExecucaoAtendimentoRepositorio {
    ExecucaoAtendimento salvar(ExecucaoAtendimento execucao);
    Optional<ExecucaoAtendimento> porId(ExecucaoAtendimentoId id);
    List<ExecucaoAtendimento> porProfissionalNoPeriodo(ProfissionalId profissionalId, LocalDateTime inicio, LocalDateTime fim);
}