package com.cesarschool.barbearia.dominio.principal.profissional.relatorio;

import java.math.BigDecimal; // Importar BigDecimal

import lombok.Builder;

public class RelatorioDesempenho {
    private final double tempoServico; 
    private final BigDecimal receitaGerada; // Alterar para BigDecimal
    private final int numeroClientesAtendidos; 
    private final double avaliacaoFuncionario; 

    @Builder
    public RelatorioDesempenho(double tempoServico,
                               BigDecimal receitaGerada, // Alterar para BigDecimal
                               int numeroClientesAtendidos,
                               double avaliacaoFuncionario) {
        this.tempoServico = tempoServico;
        this.receitaGerada = receitaGerada;
        this.numeroClientesAtendidos = numeroClientesAtendidos;
        this.avaliacaoFuncionario = avaliacaoFuncionario;
    }

    public double getTempoServico() { return tempoServico; }
    public BigDecimal getReceitaGerada() { return receitaGerada; } // Alterar para BigDecimal
    public int getNumeroClientesAtendidos() { return numeroClientesAtendidos; }
    public double getAvaliacaoFuncionario() { return avaliacaoFuncionario; }
}
