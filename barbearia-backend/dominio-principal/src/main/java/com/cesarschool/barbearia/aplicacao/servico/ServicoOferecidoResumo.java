package com.cesarschool.barbearia.aplicacao.servico;

import java.math.BigDecimal;

/**
 * Interface de projeção (DTO) para ServicoOferecido.
 * Seguindo o padrão do SGB, usa interface para permitir projeção direta do JPA.
 */
public interface ServicoOferecidoResumo {
    Integer getId();
    
    String getNome();
    
    BigDecimal getPreco();
    
    String getDescricao();
    
    Integer getDuracaoMinutos();
}
