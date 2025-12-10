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
        
        // REGRA DE NEGÓCIO ADICIONADA: Não permitir gerar relatório para uma data futura.
        if (dia.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Não é possível gerar um relatório para uma data futura.");
        }

        // Se a validação passar, delega a chamada para o próximo da cadeia.
        return super.gerarParaDia(profissionalId, dia);
    }
}
