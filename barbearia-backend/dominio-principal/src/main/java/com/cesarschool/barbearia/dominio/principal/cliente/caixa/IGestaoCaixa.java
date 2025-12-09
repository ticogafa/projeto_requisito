package com.cesarschool.barbearia.dominio.principal.cliente.caixa;

import java.math.BigDecimal;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;

public interface IGestaoCaixa {
    
    // Métodos principais (obrigatórios para quem implementa)
    void registrarEntrada(String descricao, BigDecimal valor, MeioPagamento meio);
    void registrarSaida(String descricao, BigDecimal valor, MeioPagamento meio);
    void registrarDivida(ClienteId clienteId, String descricao, BigDecimal valor, MeioPagamento meio);
    BigDecimal saldoAtual();

    // Métodos Default (Atalhos - já vêm implementados na interface!)
    default void registrarEntrada(String descricao, BigDecimal valor) {
        registrarEntrada(descricao, valor, MeioPagamento.DINHEIRO);
    }

    default void registrarSaida(String descricao, BigDecimal valor) {
        registrarSaida(descricao, valor, MeioPagamento.DINHEIRO);
    }

    default void registrarDivida(ClienteId clienteId, String descricao, BigDecimal valor) {
        registrarDivida(clienteId, descricao, valor, MeioPagamento.DINHEIRO);
    }
}