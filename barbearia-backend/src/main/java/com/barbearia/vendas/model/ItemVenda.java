package com.barbearia.vendas.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.barbearia.common.enums.TipoVenda;

import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "item_venda")
public class ItemVenda {
    UUID id; // PK
    Venda venda; // FK Venda(id), NOT NULL
    Produto produto; // FK Produto(id), opcional
    String descricao; // NOT NULL
    int quantidade = 1 ;// NOT NULL default = 1
    BigDecimal precoUnitario; // NOT NULL
    BigDecimal precoTotal; // NOT NULL
    TipoVenda tipo; // NOT NULL
}

