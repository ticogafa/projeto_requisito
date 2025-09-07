package com.barbearia.vendas.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.barbearia.common.enums.TipoVenda;
import com.barbearia.profissionais.model.ServicoOferecido;

import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Data
@Table(name = "item_venda")
@Entity
public class ItemVenda {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; // PK

    @ManyToOne(optional = false)
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;
    
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = true)
    private Produto produto; // opcional (só vai estar presente se for tipo Produto)

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = true)
    private ServicoOferecido servico; // opcional (só vai estar presente se for tipo servico)

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private int quantidade = 1;

    @Column(nullable = false)
    private BigDecimal precoUnitario;

    @Column(nullable = false)
    private BigDecimal precoTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVenda tipo;
}

