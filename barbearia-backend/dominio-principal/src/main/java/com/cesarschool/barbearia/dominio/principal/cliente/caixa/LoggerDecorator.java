package com.cesarschool.barbearia.dominio.principal.cliente.caixa;

import java.math.BigDecimal;

import com.cesarschool.barbearia.dominio.compartilhado.logger.LoggerSingleton;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;

public class LoggerDecorator extends GestaoCaixaDecorator {

    public LoggerDecorator(IGestaoCaixa proximo) {
        super(proximo);
    }

    @Override
    public void registrarEntrada(String descricao, BigDecimal valor, MeioPagamento meio) {
        String logMessage = String.format("[LOG] Registrando ENTRADA: Descrição='%s', Valor=%s, Meio=%s", descricao, valor, meio);
        LoggerSingleton.info(logMessage);
        super.registrarEntrada(descricao, valor, meio);
    }

    @Override
    public void registrarSaida(String descricao, BigDecimal valor, MeioPagamento meio) {
        String logMessage = String.format("[LOG] Registrando SAÍDA: Descrição='%s', Valor=%s, Meio=%s", descricao, valor, meio);
        LoggerSingleton.info(logMessage);
        super.registrarSaida(descricao, valor, meio);
    }

    @Override
    public void registrarDivida(ClienteId clienteId, String descricao, BigDecimal valor, MeioPagamento meio) {
        String logMessage = String.format("[LOG] Registrando DÍVIDA: Cliente=%s, Descrição='%s', Valor=%s, Meio=%s", clienteId.uuid(), descricao, valor, meio);
        LoggerSingleton.info(logMessage);
        super.registrarDivida(clienteId, descricao, valor, meio);
    }
}
