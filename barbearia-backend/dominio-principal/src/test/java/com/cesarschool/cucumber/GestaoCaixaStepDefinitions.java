package com.cesarschool.cucumber; // CORRIGIDO: Pacote corresponde à localização do arquivo

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.E;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.Lancamento;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.StatusLancamento;


public class GestaoCaixaStepDefinitions {

    private double saldoCaixa;
    private List<Lancamento> lancamentos = new ArrayList<>();
    private ClienteId clienteComDividaId;
    private static final AtomicInteger idCounter = new AtomicInteger(0); // Para gerar IDs de cliente únicos por teste

    @Dado("que o caixa da barbearia tem um saldo inicial de {double}")
    public void que_o_caixa_da_barbearia_tem_um_saldo_inicial_de(Double saldoInicial) {
        this.saldoCaixa = saldoInicial;
        this.lancamentos.clear();
    }

    @Quando("um serviço de {string} no valor de {double} é finalizado e pago")
    public void um_servico_de_no_valor_de_e_finalizado_e_pago(String servico, Double valor) {
        Lancamento lancamento = Lancamento.novaEntrada("Pagamento " + servico, valor);
        lancamentos.add(lancamento);
        this.saldoCaixa += valor;
    }

    @Quando("uma despesa de {string} no valor de {double} é registrada")
    public void uma_despesa_de_no_valor_de_e_registrada(String despesa, Double valor) {
        Lancamento lancamento = Lancamento.novaSaida(despesa, valor);
        lancamentos.add(lancamento);
        this.saldoCaixa -= valor;
    }

    @Então("o saldo final do caixa deve ser {double}")
    public void o_saldo_final_do_caixa_deve_ser(Double saldoFinal) {
        assertEquals(saldoFinal, this.saldoCaixa, 0.01);
    }

    @Quando("um cliente finaliza um serviço de {string} no valor de {double} mas não paga")
    public void um_cliente_finaliza_um_servico_de_no_valor_de_mas_nao_paga(String servico, Double valor) {
        this.clienteComDividaId = new ClienteId(idCounter.incrementAndGet()); // Simula um ID de cliente
        Lancamento divida = Lancamento.novaDivida(this.clienteComDividaId, "Dívida " + servico, valor);
        lancamentos.add(divida);
        // O saldo do caixa não é alterado
    }

    @E("uma dívida de {double} deve ser registrada para o cliente")
    public void uma_divida_de_deve_ser_registrada_para_o_cliente(Double valorDivida) {
        boolean dividaEncontrada = lancamentos.stream()
                .anyMatch(l -> l.getStatus() == StatusLancamento.PENDENTE && l.getValor() == valorDivida && l.getClienteId().equals(this.clienteComDividaId));
        assertTrue(dividaEncontrada, "A dívida não foi registrada corretamente para o cliente.");
    }

    @E("um cliente possui uma dívida pendente de {double}")
    public void um_cliente_possui_uma_divida_pendente_de(Double valorDivida) {
        this.clienteComDividaId = new ClienteId(idCounter.incrementAndGet());
        Lancamento dividaExistente = Lancamento.novaDivida(this.clienteComDividaId, "Dívida anterior", valorDivida);
        lancamentos.add(dividaExistente);
    }

    @Quando("o cliente paga a dívida de {double}")
    public void o_cliente_paga_a_divida_de(Double valorPago) {
        Optional<Lancamento> dividaParaPagar = lancamentos.stream()
                .filter(l -> l.getStatus() == StatusLancamento.PENDENTE && l.getClienteId().equals(this.clienteComDividaId))
                .findFirst();

        assertTrue(dividaParaPagar.isPresent(), "Dívida pendente do cliente não encontrada para pagamento.");
        
        Lancamento divida = dividaParaPagar.get();
        divida.quitar(); // CORREÇÃO: Usando o método de negócio da entidade
        
        // Ao quitar, uma nova entrada é gerada no caixa
        Lancamento entrada = Lancamento.novaEntrada("Pagamento de dívida", valorPago);
        lancamentos.add(entrada);
        this.saldoCaixa += valorPago;
    }

    @E("a dívida do cliente deve ser marcada como {string}")
    public void a_divida_do_cliente_deve_ser_marcada_como(String statusEsperado) {
        StatusLancamento statusEnum = StatusLancamento.valueOf(statusEsperado.toUpperCase()); // Converte String para Enum
        boolean dividaAtualizada = lancamentos.stream()
                .anyMatch(l -> l.getStatus() == statusEnum && l.getClienteId() != null && l.getClienteId().equals(this.clienteComDividaId));
        assertTrue(dividaAtualizada, "A dívida não foi marcada como " + statusEsperado);
    }
}