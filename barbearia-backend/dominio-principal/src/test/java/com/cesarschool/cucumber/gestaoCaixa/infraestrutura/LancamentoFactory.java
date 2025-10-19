package com.cesarschool.cucumber.gestaoCaixa.infraestrutura;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.Lancamento;

public final class LancamentoFactory {
    private LancamentoFactory() {}

    public static Lancamento entrada(String desc, double valor) { return Lancamento.novaEntrada(desc, valor, null); }
    public static Lancamento saida(String desc, double valor) { return Lancamento.novaSaida(desc, valor, null); }
    public static Lancamento pendente(ClienteId cliente, String desc, double valor) { return Lancamento.novaDivida(cliente, desc, valor, null); }
}