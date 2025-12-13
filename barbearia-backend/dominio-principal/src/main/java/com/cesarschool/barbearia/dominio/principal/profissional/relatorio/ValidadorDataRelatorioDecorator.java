package com.cesarschool.barbearia.dominio.principal.profissional.relatorio;

import java.time.LocalDate;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

/**
 * Concrete Decorator que adiciona uma validação na data da solicitação do relatório.
 */
public class ValidadorDataRelatorioDecorator extends GeradorRelatorioDecorator {

    public ValidadorDataRelatorioDecorator(IGeradorRelatorio proximo) {
        super(proximo);
    }

    @Override
    public RelatorioDesempenho gerarParaDia(ProfissionalId profissionalId, LocalDate dia) {
        // Removida a restrição de data futura: delega diretamente
        return super.gerarParaDia(profissionalId, dia);
    }
}
