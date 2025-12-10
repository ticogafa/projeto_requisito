package com.cesarschool.barbearia.dominio.principal.profissional.relatorio;

import java.time.LocalDate;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

public interface IRelatorioDesempenhoServico {
    RelatorioDesempenho gerarParaDia(ProfissionalId profissionalId, LocalDate dia);
}