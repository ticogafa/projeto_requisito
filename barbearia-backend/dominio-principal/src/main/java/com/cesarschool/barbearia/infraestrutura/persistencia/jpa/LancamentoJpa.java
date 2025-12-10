package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.cesarschool.barbearia.dominio.principal.cliente.caixa.MeioPagamento;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.StatusLancamento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lancamento")
public class LancamentoJpa {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "cliente_id", length = 36)
    private String clienteId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusLancamento status;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "valor", nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "meio_pagamento", nullable = false, length = 20)
    private MeioPagamento meioPagamento;

    @Column(name = "quando_data", nullable = false)
    private LocalDateTime quando;
}