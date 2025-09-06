package com.barbearia.vendas.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "venda")
public class Venda {
    private UUID vendaId;
    private UUID clienteId;
    private BigDecimal valorTotal;
    private List<ItemVenda> itens;
}