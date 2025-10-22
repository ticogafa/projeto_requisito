package com.cesarschool.barbearia.dominio.principal.profissional.relatorio;

import lombok.Builder;

public class RelatorioDesempenho {
    private final double tempoServico; 
    private final double receitaGerada; 
    private final int numeroClientesAtendidos; 
    private final double avaliacaoFuncionario; 

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
