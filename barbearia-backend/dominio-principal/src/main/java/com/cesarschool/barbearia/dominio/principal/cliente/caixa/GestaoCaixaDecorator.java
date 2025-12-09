package com.cesarschool.barbearia.dominio.principal.cliente.caixa;

import java.math.BigDecimal;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;

public abstract class GestaoCaixaDecorator implements IGestaoCaixa {
    protected final IGestaoCaixa proximo; 

    public GestaoCaixaDecorator(IGestaoCaixa proximo) {
        this.proximo = proximo;
    }

    @Override
    public void registrarEntrada(String descricao, BigDecimal valor, MeioPagamento meio) {
        proximo.registrarEntrada(descricao, valor, meio);
    }

    @Override
    public void registrarSaida(String descricao, BigDecimal valor, MeioPagamento meio) {
        proximo.registrarSaida(descricao, valor, meio);
    }

    @Override
    public void registrarDivida(ClienteId clienteId, String descricao, BigDecimal valor, MeioPagamento meio) {
        proximo.registrarDivida(clienteId, descricao, valor, meio);
    }
    
    // ... repassa todos os outros métodos

    @Override
    public BigDecimal saldoAtual() {
        return proximo.saldoAtual();
    }
}