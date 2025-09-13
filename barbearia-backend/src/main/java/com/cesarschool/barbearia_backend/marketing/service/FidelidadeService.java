package com.cesarschool.barbearia_backend.marketing.service;

import org.springframework.stereotype.Service;

import com.cesarschool.barbearia_backend.vendas.model.Venda;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service 
public class FidelidadeService {

    private static final BigDecimal FATOR_CONVERSAO_PONTOS = new BigDecimal("10.00");

    
    public int calcularPontosParaVenda(Venda vendaDTO) {
        if (vendaDTO == null || vendaDTO.getValorTotal() == null || vendaDTO.getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        
        BigDecimal pontosCalculados = vendaDTO.getValorTotal().divide(FATOR_CONVERSAO_PONTOS, 0, RoundingMode.DOWN);
        return pontosCalculados.intValue();
    }
}