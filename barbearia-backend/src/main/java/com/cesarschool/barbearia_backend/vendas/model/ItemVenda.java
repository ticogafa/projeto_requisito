package com.cesarschool.barbearia_backend.vendas.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.cesarschool.barbearia_backend.common.enums.TipoVenda;
import com.cesarschool.barbearia_backend.profissionais.model.ServicoOferecido;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
@Data
@Table(name = "item_venda")
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; // PK

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;
    
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = true)
    private Produto produto; // opcional (só vai estar presente se for tipo Produto)

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = true)
    private ServicoOferecido servico; // opcional (só vai estar presente se for tipo servico)

    @NonNull
    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private int quantidade = 1;

    @NonNull
    @Column(nullable = false)
    private BigDecimal precoUnitario;

    @NonNull
    @Column(nullable = false)
    private BigDecimal precoTotal;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVenda tipo; // sera que o tipo é necessário msm aq?
}

