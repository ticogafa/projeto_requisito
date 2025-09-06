package com.barbearia.vendas.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.barbearia.marketing.model.Cliente;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.barbearia.marketing.model.Voucher;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "venda")
public class Venda {
    private UUID id;
    private Cliente cliente;
    LocalDateTime dataVenda; // DEFAULT NOW()
    private BigDecimal valorTotal;
    private List<ItemVenda> itens; // >= 1
    private Voucher voucher; // opcional
    private String observacoes; // opcional
}

