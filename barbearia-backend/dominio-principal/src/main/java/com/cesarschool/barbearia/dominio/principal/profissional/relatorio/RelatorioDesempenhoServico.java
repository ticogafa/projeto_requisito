package com.cesarschool.barbearia.dominio.principal.profissional.relatorio;

import java.math.BigDecimal; // Importar BigDecimal
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.atendimento.ExecucaoAtendimento;
import com.cesarschool.barbearia.dominio.principal.profissional.atendimento.ExecucaoAtendimentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.profissional.avaliacao.Avaliacao;
import com.cesarschool.barbearia.dominio.principal.profissional.avaliacao.AvaliacaoRepositorio;

public class RelatorioDesempenhoServico {

    private final ExecucaoAtendimentoRepositorio execucoes;
    private final AvaliacaoRepositorio avaliacoes;

    public RelatorioDesempenhoServico(ExecucaoAtendimentoRepositorio execucoes, AvaliacaoRepositorio avaliacoes) {
        this.execucoes = execucoes;
        this.avaliacoes = avaliacoes;
    }

    public RelatorioDesempenho gerarParaDia(ProfissionalId profissionalId, LocalDate dia) {
        LocalDateTime inicio = dia.atStartOfDay();
        LocalDateTime fim = dia.plusDays(1).atStartOfDay();

        List<ExecucaoAtendimento> doDia = execucoes.porProfissionalNoPeriodo(profissionalId, inicio, fim);
        List<ExecucaoAtendimento> concluidas = doDia.stream().filter(ExecucaoAtendimento::estaConcluido).toList();

        double minutosTotais = concluidas.stream()
                .map(ExecucaoAtendimento::duracao)
                .mapToLong(Duration::toMinutes)
                .sum();

        // Lógica de soma com BigDecimal
        BigDecimal receitaTotal = concluidas.stream()
                .map(ExecucaoAtendimento::getValor) // Assumindo que getValor() retornará BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int atendimentos = concluidas.size();

        List<Avaliacao> avs = avaliacoes.porProfissionalNoPeriodo(profissionalId, inicio, fim);
        
        double media = avs.isEmpty() ? 0.0 : avs.stream()
                .mapToInt(avaliacao -> avaliacao.getNota().getValue())
                .average()
                .orElse(0.0);

        return new RelatorioDesempenho(minutosTotais, receitaTotal, atendimentos, media);
    }
}