package com.cesarschool.barbearia.dominio.principal.profissional.relatorio;

import java.time.LocalDate;

import com.cesarschool.barbearia.dominio.compartilhado.logger.LoggerSingleton;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

/**
 * Concrete Decorator do padrão Decorator.
 * 
 * Adiciona a responsabilidade de logging ao processo de geração de relatório.
 */
public class GeradorRelatorioLoggingDecorator extends GeradorRelatorioDecorator {

    public GeradorRelatorioLoggingDecorator(IGeradorRelatorio proximo) {
        super(proximo);
    }

    @Override
    public RelatorioDesempenho gerarParaDia(ProfissionalId profissionalId, LocalDate dia) {
        
        LoggerSingleton.getInstance().info(
            String.format("[LOG] Iniciando geração de relatório para o profissional %s no dia %s.", 
            profissionalId.toString(), // CORREÇÃO: Use toString() em vez de uuid()
            dia.toString()
        ));

        // Delega a chamada para o próximo componente na cadeia
        RelatorioDesempenho relatorio = super.gerarParaDia(profissionalId, dia);

        LoggerSingleton.getInstance().info(
            String.format("[LOG] Relatório gerado com sucesso: %d atendimentos, R$ %.2f de receita.",
            relatorio.getNumeroClientesAtendidos(), // Verifique se o getter é este mesmo
            relatorio.getReceitaGerada()            // Verifique se o getter é este mesmo
        ));

        return relatorio;
    }
}
