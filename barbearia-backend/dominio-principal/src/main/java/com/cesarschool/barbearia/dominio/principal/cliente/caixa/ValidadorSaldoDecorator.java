package com.cesarschool.barbearia.dominio.principal.cliente.caixa;

import java.math.BigDecimal;

public class ValidadorSaldoDecorator extends GestaoCaixaDecorator {

    public ValidadorSaldoDecorator(IGestaoCaixa proximo) {
        super(proximo);
    }

    @Override
    public void registrarSaida(String descricao, BigDecimal valor, MeioPagamento meio) {
        BigDecimal saldoAtual = proximo.saldoAtual();
        
        // Se o valor da saída for maior que o saldo, lança erro e NÃO chama o proximo
        if (valor.compareTo(saldoAtual) > 0) {
            throw new IllegalStateException("Operação bloqueada: Saldo insuficiente para realizar esta saída.");
        }

        super.registrarSaida(descricao, valor, meio);
    }
}