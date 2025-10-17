package com.cesarschool.barbearia.dominio.principal.profissional.relatorio;

public class RelatorioDesempenho {
    // minutos totais de serviço no dia
    private final double tempo_servico;
    // soma dos valores dos serviços concluídos no dia
    private final double receita_gerada;
    // quantidade de atendimentos concluídos no dia (ou clientes únicos, se preferir)
    private final int numero_clientes_atendidos;
    // média das avaliações do dia (1..5), 0.0 se não houver
    private final double avaliacao_funcionario;

    public RelatorioDesempenho(double tempo_servico,
                               double receita_gerada,
                               int numero_clientes_atendidos,
                               double avaliacao_funcionario) {
        this.tempo_servico = tempo_servico;
        this.receita_gerada = receita_gerada;
        this.numero_clientes_atendidos = numero_clientes_atendidos;
        this.avaliacao_funcionario = avaliacao_funcionario;
    }

    public double getTempo_servico() { return tempo_servico; }
    public double getReceita_gerada() { return receita_gerada; }
    public int getNumero_clientes_atendidos() { return numero_clientes_atendidos; }
    public double getAvaliacao_funcionario() { return avaliacao_funcionario; }
}
