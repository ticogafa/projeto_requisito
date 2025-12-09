package com.cesarschool.cucumber.gestaoCaixa;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.cesarschool.barbearia.dominio.principal.cliente.caixa.GestaoCaixaServico;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.IGestaoCaixa;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.ValidadorSaldoDecorator;
import com.cesarschool.cucumber.gestaoCaixa.infraestrutura.LancamentoMockRepositorio;

public class GestaoCaixaDecoratorTest {

    @Test
    public void deveBloquearSaidaQuandoSaldoInsuficiente() {
        // 1. Configuração (Setup)
        LancamentoMockRepositorio repo = new LancamentoMockRepositorio();
        IGestaoCaixa servicoBase = new GestaoCaixaServico(repo);
        
        // 2. Aplica o Decorator de Validação
        IGestaoCaixa servicoSeguro = new ValidadorSaldoDecorator(servicoBase);

        // 3. Adiciona um saldo inicial de R$ 100,00
        servicoSeguro.registrarEntrada("Saldo Inicial", new BigDecimal("100.00"));

        // 4. Tenta registrar uma saída de R$ 150,00 (Maior que o saldo)
        // Esperamos que lance uma IllegalStateException
        assertThrows(IllegalStateException.class, () -> {
            servicoSeguro.registrarSaida("Compra de Equipamento", new BigDecimal("150.00"));
        });

        // 5. Verifica se o saldo permaneceu intacto (R$ 100,00) e a saída não foi registrada
        assertEquals(0, new BigDecimal("100.00").compareTo(servicoSeguro.saldoAtual()), 
            "O saldo não deveria ter mudado após a tentativa falha.");
    }

    @Test
    public void devePermitirSaidaQuandoSaldoSuficiente() {
        // 1. Configuração
        LancamentoMockRepositorio repo = new LancamentoMockRepositorio();
        IGestaoCaixa servicoBase = new GestaoCaixaServico(repo);
        IGestaoCaixa servicoSeguro = new ValidadorSaldoDecorator(servicoBase);

        // 2. Adiciona saldo de R$ 100,00
        servicoSeguro.registrarEntrada("Saldo Inicial", new BigDecimal("100.00"));

        // 3. Tenta registrar uma saída de R$ 40,00 (Menor que o saldo)
        servicoSeguro.registrarSaida("Conta de Luz", new BigDecimal("40.00"));

        // 4. Verifica se o saldo foi descontado corretamente (100 - 40 = 60)
        assertEquals(0, new BigDecimal("60.00").compareTo(servicoSeguro.saldoAtual()), 
            "O saldo deveria ter sido descontado corretamente.");
    }
}