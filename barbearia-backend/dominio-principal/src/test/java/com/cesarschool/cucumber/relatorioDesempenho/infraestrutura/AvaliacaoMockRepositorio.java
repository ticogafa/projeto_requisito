package com.cesarschool.cucumber.relatorioDesempenho.infraestrutura;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.avaliacao.Avaliacao;
import com.cesarschool.barbearia.dominio.principal.profissional.avaliacao.AvaliacaoRepositorio;

public class AvaliacaoMockRepositorio implements AvaliacaoRepositorio {

    private final List<Avaliacao> store = new ArrayList<>();

    public void limpar() { store.clear(); }

    @Override
    public Avaliacao salvar(Avaliacao avaliacao) {
        store.add(avaliacao);
        return avaliacao;
    }

    @Override
    public List<Avaliacao> porProfissionalNoPeriodo(ProfissionalId profissionalId, LocalDateTime inicio, LocalDateTime fim) {
        return store.stream()
            .filter(a -> a.getProfissionalId().equals(profissionalId))
            .filter(a -> a.getQuando().isAfter(inicio) && a.getQuando().isBefore(fim))
            .collect(Collectors.toList());
    }
}