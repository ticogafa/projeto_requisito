package com.barbearia.marketing.domain.service;

import com.barbearia.marketing.application.dto.VendaDTO;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service 
public class FidelidadeService {

    private static final BigDecimal FATOR_CONVERSAO_PONTOS = new BigDecimal("10.00");

    
    public int calcularPontosParaVenda(VendaDTO vendaDTO) {
        if (vendaDTO == null || vendaDTO.valorTotal() == null || vendaDTO.valorTotal().compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        
        BigDecimal pontosCalculados = vendaDTO.valorTotal().divide(FATOR_CONVERSAO_PONTOS, 0, RoundingMode.DOWN);
        return pontosCalculados.intValue();
    }
}