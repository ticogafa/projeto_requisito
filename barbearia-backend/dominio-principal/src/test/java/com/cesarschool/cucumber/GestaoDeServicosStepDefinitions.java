package com.cesarschool.cucumber;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoServico;
import com.cesarschool.cucumber.servico.ServicoOferecidoMockRepositorio;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class GestaoDeServicosStepDefinitions {

    private ServicoOferecidoMockRepositorio repositorioMock;
    private ServicoOferecidoServico servicoOferecidoServico;
    private ServicoOferecido servicoInserir;
    private ServicoOferecido servicoCriado;

    private final ProfissionalId profissionalIdForTestId = new ProfissionalId(1);

    // private Exception captured;

    @Before
    public void setup() {
        this.repositorioMock = new ServicoOferecidoMockRepositorio();
        this.servicoOferecidoServico = new ServicoOferecidoServico(repositorioMock);
        this.repositorioMock.limpar();
        this.servicoInserir = null;
        this.servicoCriado = null;
        // this.captured = null;
    }

    @Given("que não existe um serviço chamado {string}")
    public void que_não_existe_um_serviço_chamado(String string) {
        ServicoOferecido service = repositorioMock.buscarPorNome(string);
        Assertions.assertNull(service, "O serviço não deveria existir");
    }
    @When("eu crio um novo serviço com o nome {string}")
    public void eu_crio_um_novo_serviço_com_o_nome(String string) {
       servicoInserir = new ServicoOferecido(
        profissionalIdForTestId,
        string,
        new BigDecimal("100.00"),
        "Descricao muito criativa",
        101
       );

       servicoCriado = servicoOferecidoServico.registrar(servicoInserir);
    }
    @Then("o serviço é criado com sucesso")
    public void o_serviço_é_criado_com_sucesso() {
        Assertions.assertNotNull(servicoCriado, "O serviço criado não deveria ser nulo");
        Assertions.assertNotNull(servicoInserir.getNome(), "O ID não deveria ser nulo");

        ServicoOferecido servicoPersistido = repositorioMock.buscarPorIdOptional(servicoCriado.getId().getValor()).orElse(null);

        Assertions.assertNotNull(servicoPersistido, "O serviço deve ter sido salvo no repositório");
        Assertions.assertEquals(servicoInserir.getNome(), servicoPersistido.getNome(), "O nome do serviço persistido deve coincidir");
    }
}
