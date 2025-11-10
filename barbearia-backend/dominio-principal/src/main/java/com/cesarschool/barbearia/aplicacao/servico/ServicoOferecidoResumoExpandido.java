package com.cesarschool.barbearia.aplicacao.servico;

/**
 * Interface de projeção expandida (DTO) para ServicoOferecido.
 * Inclui informações adicionais além do resumo básico.
 * Seguindo o padrão do SGB para casos onde mais detalhes são necessários.
 */
public interface ServicoOferecidoResumoExpandido extends ServicoOferecidoResumo {
    
    /**
     * Retorna o preço formatado como moeda brasileira.
     * @return Preço formatado (ex: "R$ 50,00")
     */
    default String getPrecoFormatado() {
        return String.format("R$ %.2f", getPreco());
    }
    
    /**
     * Retorna a duração formatada em horas e minutos.
     * @return Duração formatada (ex: "1h 30min" ou "45min")
     */
    default String getDuracaoFormatada() {
        int minutos = getDuracaoMinutos();
        int horas = minutos / 60;
        int minutosRestantes = minutos % 60;
        
        if (horas > 0 && minutosRestantes > 0) {
            return String.format("%dh %dmin", horas, minutosRestantes);
        } else if (horas > 0) {
            return String.format("%dh", horas);
        } else {
            return String.format("%dmin", minutos);
        }
    }
}
