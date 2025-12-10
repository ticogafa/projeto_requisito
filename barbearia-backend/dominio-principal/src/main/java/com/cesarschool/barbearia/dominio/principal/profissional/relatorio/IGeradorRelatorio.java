package com.cesarschool.barbearia.dominio.principal.profissional.relatorio;

import java.time.LocalDate;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

/**
 * Componente do padrão Decorator.
 * Define a interface para objetos que geram relatórios de desempenho.
 */
public interface IGeradorRelatorio {
    RelatorioDesempenho gerarParaDia(ProfissionalId profissionalId, LocalDate dia);
}
