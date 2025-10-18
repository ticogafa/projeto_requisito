package com.cesarschool.cucumber.relatorioDesempenho.infraestrutura;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ExecucaoAtendimentoId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.atendimento.ExecucaoAtendimento;
import com.cesarschool.barbearia.dominio.principal.profissional.atendimento.ExecucaoAtendimentoRepositorio;

public class ExecucaoAtendimentoMockRepositorio implements ExecucaoAtendimentoRepositorio {

    private final List<ExecucaoAtendimento> store = new ArrayList<>();

    public void limpar() { store.clear(); }

    @Override
    public ExecucaoAtendimento salvar(ExecucaoAtendimento execucao) {
        store.removeIf(e -> e.getId().equals(execucao.getId()));
        store.add(execucao);
        return execucao;
    }

    @Override
    public Optional<ExecucaoAtendimento> porId(ExecucaoAtendimentoId id) {
        return store.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public List<ExecucaoAtendimento> porProfissionalNoPeriodo(ProfissionalId profissionalId, LocalDateTime inicio, LocalDateTime fim) {
        return store.stream()
            .filter(e -> e.getProfissionalId().equals(profissionalId))
            .filter(e -> !e.getInicio().isBefore(inicio) && e.getInicio().isBefore(fim))
            .collect(Collectors.toList());
    }
}