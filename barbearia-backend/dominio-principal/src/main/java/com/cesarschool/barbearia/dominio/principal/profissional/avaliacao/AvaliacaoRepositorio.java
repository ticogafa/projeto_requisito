package com.cesarschool.barbearia.dominio.principal.profissional.avaliacao;

import java.time.LocalDateTime;
import java.util.List;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

public interface AvaliacaoRepositorio {
    Avaliacao salvar(Avaliacao avaliacao);
    List<Avaliacao> porProfissionalNoPeriodo(ProfissionalId profissionalId, LocalDateTime inicio, LocalDateTime fim);
}