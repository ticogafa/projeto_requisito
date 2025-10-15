package com.cesarschool.cucumber.agendamento;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;
import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoServico;
import com.cesarschool.barbearia.dominio.principal.agendamento.StatusAgendamento;
import com.cesarschool.barbearia.dominio.principal.cliente.Cliente;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AgendamentoTest {

    private Cliente cliente;
    private LocalDateTime horario;
    private Agendamento agendamento;
    private Agendamento agendamentoSalvo;
    private AgendamentoRepositorio repositorio = new AgendamentoMockRepositorio();
    private AgendamentoServico servico = new AgendamentoServico(repositorio);
    private Exception excecaoLancada;

    @Given("que escolho um horário futuro livre para o profissional")
    public void que_escolho_um_horário_futuro_livre_para_o_profissional() {
        horario = LocalDateTime.now().plusHours(2);
    }

    @Given("que informo as informações essenciais")
    public void que_informo_as_informações_essenciais() {
        cliente = new Cliente(
                new ClienteId(1),
                "João Silva",
                new Email("joao@email.com"),
                new Cpf("53604042801"),
                new Telefone("81999999999"));

        agendamento = new Agendamento(
                horario,
                cliente.getId(),
                new ProfissionalId(1),
                new ServicoOferecidoId(1),
                "Nenhuma observação");
    }

    @When("solicito a criação do agendamento")
    public void solicito_a_criação_do_agendamento() {
        agendamentoSalvo = servico.criar(
                agendamento,
                30);
    }

    @Then("o sistema responde sucesso")
    public void o_sistema_responde_sucesso() {
        assertNotNull("Agendamento não deve ser nulo", agendamentoSalvo);
        assertEquals("Status inicial deve ser PENDENTE", StatusAgendamento.PENDENTE, agendamentoSalvo.getStatus());
        assertTrue("Horário deve ser futuro", agendamentoSalvo.getDataHora().isAfter(LocalDateTime.now()));
        assertEquals(agendamento.getClienteId(), agendamentoSalvo.getClienteId());
        assertEquals(agendamento.getProfissionalId(), agendamentoSalvo.getProfissionalId());
        assertEquals(agendamento.getServicoId(), agendamentoSalvo.getServicoId());
    }

    //The step 'que já existe um agendamento para "João Barbeiro" no mesmo horário' and 2 other step(s) are undefined.

    @Given("que já existe um agendamento para outro cliente no mesmo horário")
    public void queJáExisteUmAgendamentoParaNoMesmoHorário() {
        repositorio = new AgendamentoConflitoRepositorio();
        servico = new AgendamentoServico(repositorio);
    }

    @Given("que tento criar outro agendamento nesse horário com os mesmos Givens essenciais")
    public void queTentoCriarOutroAgendamentoNesseHorárioComOsMesmosGivensEssenciais() {
        horario = LocalDateTime.now().plusHours(2);
        cliente = new Cliente(
                new ClienteId(1),
                "João Silva",
                new Email("joao@email.com"),
                new Cpf("53604042801"),
                new Telefone("81999999999"));

        agendamento = new Agendamento(
                horario,
                cliente.getId(),
                new ProfissionalId(1),
                new ServicoOferecidoId(1),
                "Nenhuma observação"
                );

    }

    @When("solicito a criação do novo agendamento")
    public void solicitoACriaçãoDoNovoAgendamento() {
        try {
            agendamentoSalvo = servico.criar(
                    agendamento,
                    30);
        } catch (Exception e) {
            this.excecaoLancada = e;
        }
    }

    @Then("o sistema rejeita a operação de agendamento com conflito")
    public void oSistemaRejeitaAOperaçãoDeAgendamentoComConflito() {
    assertNotNull("Exceção deve ter sido lançada", this.excecaoLancada);
}

// // que escolho um agendamento inválido às 02:00 fora da jornada do profissional' and 1 other step(s) are undefined.

// @Given("que escolho um agendamento inválido às {int}:{int} fora da jornada do profissional")
// public void queEscolhoUmAgendamentoInválidoÀsForaDaJornadaDoProfissional(Integer int1, Integer int2) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Then("o sistema rejeita a operação de agendamento fora da jornada")
// public void oSistemaRejeitaAOperaçãoDeAgendamentoForaDaJornada() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }



// // io.cucumber.junit.UndefinedStepException: The step 'que existe um agendamento marcado para ocorrer em menos de 2 horas' and 3 other step(s) are undefined.

// @Given("que existe um agendamento marcado para ocorrer em menos de {int} horas")
// public void queExisteUmAgendamentoMarcadoParaOcorrerEmMenosDeHoras(Integer int1) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Given("que o status atual do agendamento é {string}")
// public void queOStatusAtualDoAgendamentoÉ(String string) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @When("solicito o cancelamento deste agendamento")
// public void solicitoOCancelamentoDesteAgendamento() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Then("o sistema rejeita a operação de cancelamento por antecedência insuficiente")
// public void oSistemaRejeitaAOperaçãoDeCancelamentoPorAntecedênciaInsuficiente() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }


// // io.cucumber.junit.UndefinedStepException: The step 'que não informei um profissional explicitamente' and 1 other step(s) are undefined.

// @Given("que não informei um profissional explicitamente")
// public void queNãoInformeiUmProfissionalExplicitamente() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Then("o sistema responde sucesso e atribui um profissional automaticamente")
// public void oSistemaRespondeSucessoEAtribuiUmProfissionalAutomaticamente() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }

// // The step 'que não informei um profissional explicitamente' and 3 other step(s) are undefined.

// @Given("que não informei um profissional explicitamente")
// public void queNãoInformeiUmProfissionalExplicitamente() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Given("que escolho um horário futuro livre às {int}:{int}")
// public void queEscolhoUmHorárioFuturoLivreÀs(Integer int1, Integer int2) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Then("o sistema deve exibir a mensagem {string}")
// public void oSistemaDeveExibirAMensagem(String string) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Then("o sistema deve exibir o profissional atribuído automaticamente")
// public void oSistemaDeveExibirOProfissionalAtribuídoAutomaticamente() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }


}