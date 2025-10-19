package com.cesarschool.cucumber.gestaoCaixa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.GestaoCaixaServico;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.MeioPagamento;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.StatusLancamento;
import com.cesarschool.cucumber.gestaoCaixa.infraestrutura.LancamentoMockRepositorio;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class GestaoCaixaStepDefinitions {

    private double saldoInicial;
    private final LancamentoMockRepositorio repo = new LancamentoMockRepositorio();
    private final GestaoCaixaServico servico = new GestaoCaixaServico(repo);
    private ClienteId clienteComDividaId;

    @Dado("que o caixa da barbearia tem um saldo inicial de {double}")
    public void caixa_saldo_inicial(Double saldoInicial) {
        this.saldoInicial = saldoInicial;
        repo.limpar();
    }

    @Dado("um cliente possui uma dívida pendente de {double}")
    public void umClientePossuiUmaDívidaPendenteDe(Double double1) {
        this.clienteComDividaId = new ClienteId(1);
        this.servico.registrarDivida(this.clienteComDividaId, "Dívida existente", double1);
    }

    // passo antigo mantido (sem meio) - compatibilidade
    @Quando("um serviço de {string} no valor de {double} é finalizado e pago")
    public void servico_pago(String servico, Double valor) {
        servico = ("Pagamento " + servico).trim();
        this.servico.registrarEntrada(servico, valor);
    }

    // novo passo que recebe o meio de pagamento
    @Quando("um serviço de {string} no valor de {double} é finalizado e pago via {string}")
    public void servico_pago_via(String servico, Double valor, String meio) {
        servico = ("Pagamento " + servico).trim();
        MeioPagamento mp = MeioPagamento.valueOf(meio.toUpperCase());
        this.servico.registrarEntrada(servico, valor, mp);
    }

    @Quando("uma despesa de {string} no valor de {double} é registrada")
    public void despesa_registrada(String despesa, Double valor) {
        this.servico.registrarSaida(despesa, valor);
    }

    @Entao("o saldo final do caixa deve ser {double}")
    public void saldo_final(Double esperado) {
        double saldoFinal = saldoInicial + this.servico.saldoAtual();
        assertEquals(esperado, saldoFinal, 0.01);
    }

    @Quando("um cliente finaliza um serviço de {string} no valor de {double} mas não paga")
    public void servico_nao_pago(String servico, Double valor) {
        this.clienteComDividaId = new ClienteId(1);
        this.servico.registrarDivida(this.clienteComDividaId, "Dívida " + servico, valor);
    }

    @E("uma dívida de {double} deve ser registrada para o cliente")
    public void verifica_divida(Double valorDivida) {
        boolean divida = repo.buscarPendentesPorCliente(this.clienteComDividaId).stream()
            .anyMatch(l -> l.getStatus() == StatusLancamento.PENDENTE
                        && l.getValor() == valorDivida
                        && this.clienteComDividaId.equals(l.getClienteId()));
        assertTrue(divida, "Dívida não registrada para o cliente.");
    }

    @E(" um cliente possui uma dívida pendente de {double}")
    public void cliente_possui_divida(Double valorDivida) {
        // garante a existência da dívida neste cenário
        this.clienteComDividaId = new ClienteId(1);
        this.servico.registrarDivida(this.clienteComDividaId, "Dívida existente", valorDivida);
    }

    // passo antigo mantido (sem meio) - compatibilidade
    @Quando("o cliente paga a dívida de {double}")
    public void o_cliente_paga_a_divida_de(Double valorPago) {
        if (this.clienteComDividaId == null) {
            this.clienteComDividaId = new com.cesarschool.barbearia.dominio.principal.cliente.ClienteId(1);
            this.servico.registrarDivida(this.clienteComDividaId, "Dívida", valorPago);
        }
        this.servico.pagarPrimeiraDivida(this.clienteComDividaId, valorPago);
    }

    // novo passo que indica o meio de pagamento para quitar a dívida
    @Quando("o cliente paga a dívida de {double} via {string}")
    public void o_cliente_paga_a_divida_de_via(Double valorPago, String meio) {
        if (this.clienteComDividaId == null) {
            this.clienteComDividaId = new com.cesarschool.barbearia.dominio.principal.cliente.ClienteId(1);
            this.servico.registrarDivida(this.clienteComDividaId, "Dívida", valorPago);
        }
        MeioPagamento mp = MeioPagamento.valueOf(meio.toUpperCase());
        this.servico.pagarPrimeiraDivida(this.clienteComDividaId, valorPago, mp);
    }

    @E("a dívida do cliente deve ser marcada como {string}")
    public void a_divida_do_cliente_deve_ser_marcada_como(String status) {
        var esperado = com.cesarschool.barbearia.dominio.principal.cliente.caixa.StatusLancamento.valueOf(status.toUpperCase());

        var doCliente = repo.buscarTodos().stream()
            .filter(l -> this.clienteComDividaId != null && this.clienteComDividaId.equals(l.getClienteId()))
            .toList();

        boolean temEsperado = doCliente.stream().anyMatch(l -> l.getStatus() == esperado);
        if (esperado == com.cesarschool.barbearia.dominio.principal.cliente.caixa.StatusLancamento.PAGO) {
            temEsperado = temEsperado && doCliente.stream().noneMatch(l ->
                l.getStatus() == com.cesarschool.barbearia.dominio.principal.cliente.caixa.StatusLancamento.PENDENTE
            );
        }

        org.junit.jupiter.api.Assertions.assertTrue(temEsperado, "Esperado status " + esperado + " para a dívida do cliente.");
    }

}