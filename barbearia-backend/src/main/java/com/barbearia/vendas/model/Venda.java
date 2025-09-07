package com.barbearia.vendas.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.barbearia.marketing.model.Cliente;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    
    @ManyToOne
    @JoinColumn(name = "cliente", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    LocalDateTime dataVenda = LocalDateTime.now();

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @OneToMany(mappedBy = "item_venda")
    private List<ItemVenda> itens = new ArrayList<>(); // >= 1

    @Column(nullable = true)
    private Voucher voucher; // opcional

    @Column(nullable = true)
    private String observacoes; // opcional
}

