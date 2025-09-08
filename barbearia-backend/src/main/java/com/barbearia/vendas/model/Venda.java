package com.barbearia.vendas.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.barbearia.marketing.model.Cliente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.barbearia.marketing.model.Voucher;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "venda")
public class Venda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NonNull
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime dataVenda = LocalDateTime.now();

    @NonNull
    @Column(nullable = false)
    private BigDecimal valorTotal;

    @OneToMany(mappedBy = "venda")
    private List<ItemVenda> itens = new ArrayList<>(); // >= 1

    @ManyToOne
    @JoinColumn(name = "voucher_id", nullable = true)
    private Voucher voucher; // opcional

    @Column(nullable = true)
    private String observacoes; // opcional
}

