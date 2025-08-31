package com.barbearia.marketing.application.dto;

import java.math.BigDecimal;
import java.util.UUID;


public record VendaDTO(UUID vendaId, UUID clienteId, BigDecimal valorTotal) {
}