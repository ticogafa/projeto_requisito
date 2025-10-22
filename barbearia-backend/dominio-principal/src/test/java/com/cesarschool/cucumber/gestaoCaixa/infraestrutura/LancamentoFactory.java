package com.cesarschool.cucumber.gestaoCaixa.infraestrutura;

import java.math.BigDecimal;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.Lancamento;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.MeioPagamento;

public final class LancamentoFactory {
    private LancamentoFactory() {}

    public static Lancamento entrada(String desc, double valor) { 
        return Lancamento.novoRecibemento(desc, BigDecimal.valueOf(valor), MeioPagamento.DINHEIRO); 
    }
    public static Lancamento saida(String desc, double valor) { 
        return Lancamento.novoGasto(desc, BigDecimal.valueOf(valor), MeioPagamento.DINHEIRO); 
    }
    public static Lancamento pendente(ClienteId cliente, String desc, double valor) { 
        return Lancamento.novaDivida(cliente, desc, BigDecimal.valueOf(valor), MeioPagamento.DINHEIRO); 
    }
}