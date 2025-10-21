package com.cesarschool.barbearia.dominio.principal.profissional.relatorio;

import lombok.Builder;

public class RelatorioDesempenho {
    private final double tempoServico; // minutos totais
    private final double receitaGerada; // soma dos valores
    private final int numeroClientesAtendidos; // quantidade de execuções concluídas
    private final double avaliacaoFuncionario; // média 1..5

    @Builder
    public RelatorioDesempenho(double tempoServico,
                               double receitaGerada,
                               int numeroClientesAtendidos,
                               double avaliacaoFuncionario) {
        this.tempoServico = tempoServico;
        this.receitaGerada = receitaGerada;
        this.numeroClientesAtendidos = numeroClientesAtendidos;
        this.avaliacaoFuncionario = avaliacaoFuncionario;
    }

    public double getTempoServico() { return tempoServico; }
    public double getReceitaGerada() { return receitaGerada; }
    public int getNumeroClientesAtendidos() { return numeroClientesAtendidos; }
    public double getAvaliacaoFuncionario() { return avaliacaoFuncionario; }
}
