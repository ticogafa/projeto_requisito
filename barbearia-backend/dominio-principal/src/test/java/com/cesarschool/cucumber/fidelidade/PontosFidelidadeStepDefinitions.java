package com.cesarschool.cucumber.fidelidade;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;
import com.cesarschool.barbearia.dominio.principal.cliente.Cliente;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.cliente.pontos.PontosFidelidadeServico;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PontosFidelidadeStepDefinitions {

    private ClienteRepositorioMock clienteRepositorio;
    private PontosFidelidadeServico pontosServico;
    private Cliente cliente;
    private Exception excecao;

    @Before
    public void setup() {
        clienteRepositorio = new ClienteRepositorioMock();
        pontosServico = new PontosFidelidadeServico(clienteRepositorio);
        clienteRepositorio.limpar();
        cliente = null;
        excecao = null;
    }

    @Given("um cliente com 0 pontos")
    public void um_cliente_com_zero_pontos() {
        cliente = new Cliente(
            new ClienteId(1),
            "Cliente Fidelidade",
            new Email("cliente@teste.com"),
            new Cpf("12345678901"),
            new Telefone("81999999999"),
            0
        );
        clienteRepositorio.salvar(cliente);
    }

    @When("registrar um atendimento no valor de {double} reais")
    public void registrar_atendimento_valor(Double valor) {
        try {
            pontosServico.creditar(cliente.getId(), BigDecimal.valueOf(valor));
        } catch (Exception e) {
            excecao = e;
        }
    }

    @Then("o cliente deve ter {int} pontos de fidelidade")
    public void cliente_deve_ter_pontos(Integer pontosEsperados) {
        assertNull(excecao, "Nenhuma exceção deveria ocorrer");
        Cliente atualizado = clienteRepositorio.buscarPorId(cliente.getId().getValor());
        assertEquals(pontosEsperados.intValue(), atualizado.getPontos());
    }
}
