package com.cesarschool.cucumber.gestaoCaixa;

import java.math.BigDecimal;

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

    private BigDecimal saldoInicial;
    private final LancamentoMockRepositorio repo = new LancamentoMockRepositorio();
    private final GestaoCaixaServico servico = new GestaoCaixaServico(repo);
    private ClienteId clienteComDividaId;

    private BigDecimal parseBigDecimalPt(String valorStr) {
        return new BigDecimal(valorStr.replace(',', '.'));
    }

    @Dado("que o caixa da barbearia tem um saldo inicial de {string}")
    public void caixa_saldo_inicial(String saldoInicialStr) {
        this.saldoInicial = parseBigDecimalPt(saldoInicialStr);
        repo.limpar();
    }

    @Dado("um cliente possui uma dívida pendente de {string}")
    public void um_cliente_possui_uma_divida_pendente_de(String valorDividaStr) {
        this.clienteComDividaId = new ClienteId(1);
        this.servico.registrarDivida(this.clienteComDividaId, "Dívida existente", parseBigDecimalPt(valorDividaStr));
    }

    @Quando("um serviço de {string} no valor de {string} é pago em {string}")
    public void servico_pago_em(String descricao, String valorStr, String meio) {
        String servico = ("Pagamento " + descricao).trim();
        BigDecimal valor = parseBigDecimalPt(valorStr);
        MeioPagamento mp = MeioPagamento.valueOf(meio.toUpperCase());
        this.servico.registrarEntrada(servico, valor, mp);
    }

    @Quando("uma despesa de {string} no valor de {string} é registrada")
    public void despesa_registrada(String despesa, String valorStr) {
        this.servico.registrarSaida(despesa, parseBigDecimalPt(valorStr));
    }

    @Quando("um cliente finaliza um serviço de {string} no valor de {string} mas não paga")
    public void servico_nao_pago(String servico, String valorStr) {
        this.clienteComDividaId = new ClienteId(1);
        this.servico.registrarDivida(this.clienteComDividaId, "Dívida " + servico, parseBigDecimalPt(valorStr));
    }

    @Quando("o cliente paga a dívida de {string} em {string}")
    public void o_cliente_paga_a_divida_de_em(String valorPagoStr, String meio) {
        if (this.clienteComDividaId == null) {
            this.clienteComDividaId = new ClienteId(1);
        }
        BigDecimal valorPago = parseBigDecimalPt(valorPagoStr);
        MeioPagamento mp = MeioPagamento.valueOf(meio.toUpperCase());
        this.servico.pagarPrimeiraDivida(this.clienteComDividaId, valorPago, mp);
    }

    @Entao("o saldo final do caixa deve ser {string}")
    public void saldo_final(String esperadoStr) {
        BigDecimal esperado = parseBigDecimalPt(esperadoStr);
        BigDecimal saldoFinal = saldoInicial.add(this.servico.saldoAtual());
        assertEquals(0, esperado.compareTo(saldoFinal), "O saldo final do caixa não é o esperado.");
    }

    @E("uma dívida de {string} deve ser registrada para o cliente")
    public void verifica_divida(String valorDividaStr) {
        BigDecimal valorDivida = parseBigDecimalPt(valorDividaStr);
        boolean divida = repo.buscarPendentesPorCliente(this.clienteComDividaId).stream()
            .anyMatch(l -> l.getStatus() == StatusLancamento.PENDENTE
                        && valorDivida.compareTo(l.getValor()) == 0
                        && this.clienteComDividaId.equals(l.getClienteId()));
        assertTrue(divida, "Dívida não registrada para o cliente.");
    }

    @E("a dívida do cliente deve ser marcada como {string}")
    public void a_divida_do_cliente_deve_ser_marcada_como(String status) {
        var esperado = StatusLancamento.valueOf(status.toUpperCase());
        var doCliente = repo.buscarTodosPorCliente(this.clienteComDividaId);

        boolean temPago = doCliente.stream().anyMatch(l -> l.getStatus() == esperado);
        boolean naoTemPendente = doCliente.stream().noneMatch(l -> l.getStatus() == StatusLancamento.PENDENTE);

        assertTrue(temPago, "A dívida não foi marcada como PAGO.");
        assertTrue(naoTemPendente, "Ainda existe uma dívida PENDENTE para o cliente.");
    }
}