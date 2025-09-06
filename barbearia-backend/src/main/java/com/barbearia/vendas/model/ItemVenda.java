package com.barbearia.vendas.model;

import java.math.BigDecimal;

import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "item_venda")
public class ItemVenda {
    private String descricao;
    private BigDecimal preco;
    private int quantidade; 
}
