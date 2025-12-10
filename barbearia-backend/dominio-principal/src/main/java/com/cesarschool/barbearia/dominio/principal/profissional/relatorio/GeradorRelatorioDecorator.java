package com.cesarschool.barbearia.dominio.principal.profissional.relatorio;

import java.time.LocalDate;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

/**
 * Decorator Abstrato do padrão Decorator.
 * 
 * Mantém uma referência para o objeto Component (IGeradorRelatorio) e
 * delega a chamada para ele. As classes filhas (Concrete Decorators)
 * podem adicionar seu comportamento antes ou depois de delegar a chamada.
 */
public abstract class GeradorRelatorioDecorator implements IGeradorRelatorio {

    protected final IGeradorRelatorio proximo;

    public GeradorRelatorioDecorator(IGeradorRelatorio proximo) {
        this.proximo = proximo;
    }

    @Override
    public RelatorioDesempenho gerarParaDia(ProfissionalId profissionalId, LocalDate dia) {
        return proximo.gerarParaDia(profissionalId, dia);
    }
}
