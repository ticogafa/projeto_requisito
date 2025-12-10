package com.cesarschool.barbearia.infraestrutura.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cesarschool.barbearia.dominio.principal.profissional.atendimento.ExecucaoAtendimentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.profissional.avaliacao.AvaliacaoRepositorio;
import com.cesarschool.barbearia.dominio.principal.profissional.relatorio.GeradorRelatorioLoggingDecorator;
import com.cesarschool.barbearia.dominio.principal.profissional.relatorio.IGeradorRelatorio;
import com.cesarschool.barbearia.dominio.principal.profissional.relatorio.RelatorioDesempenhoServico;
import com.cesarschool.barbearia.dominio.principal.profissional.relatorio.ValidadorDataRelatorioDecorator;

@Configuration
public class RelatorioConfig {

    @Bean
    public IGeradorRelatorio relatorioDesempenhoServico(
            ExecucaoAtendimentoRepositorio repoExecucoes,
            AvaliacaoRepositorio repoAvaliacoes
    ) {
        // 1. Cria o serviço base (ConcreteComponent)
        IGeradorRelatorio servicoBase = new RelatorioDesempenhoServico(repoExecucoes, repoAvaliacoes);

        // 2. Envolve com o Decorator de validação de data
        IGeradorRelatorio comValidador = new ValidadorDataRelatorioDecorator(servicoBase);

        // 3. Envolve com o Decorator de log e retorna a cadeia completa
        IGeradorRelatorio comLog = new GeradorRelatorioLoggingDecorator(comValidador);
        
        return comLog;
    }
}