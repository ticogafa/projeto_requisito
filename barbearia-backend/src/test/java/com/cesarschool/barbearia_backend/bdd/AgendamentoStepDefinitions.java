package com.cesarschool.barbearia_backend.bdd;

import org.springframework.beans.factory.annotation.Autowired;

import com.cesarschool.barbearia_backend.helper.TestEntityFactory;
import com.cesarschool.barbearia_backend.agendamento.service.AgendamentoService;
import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.CriarAgendamentoRequest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import com.cesarschool.barbearia_backend.common.enums.DiaSemana;

import static org.junit.jupiter.api.Assertions.*;


public class AgendamentoStepDefinitions extends CucumberSpringContext {

    @Autowired
    private TestEntityFactory factory;

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired private AgendamentoContext ctx;

    @Given("que escolho um horário futuro livre para o profissional")
    public void que_escolho_um_horário_futuro_livre_para_o_profissional() {
        // Criar dados de teste usando a factory
    ctx.cliente = factory.saveCliente("João Silva", "joao@email.com", "12345678901", "11999999999");

    ctx.profissional = factory.saveProfissionalComJornada(
            "Carlos Barbeiro",
            "carlos@barbearia.com",
            "98765432100",
            "11888888888",
            java.time.LocalTime.of(8, 0),
            java.time.LocalTime.of(18, 0),
            DiaSemana.SEGUNDA,
            DiaSemana.TERCA,
            DiaSemana.QUARTA,
            DiaSemana.QUINTA,
            DiaSemana.SEXTA
        );

    ctx.servico = factory.saveServico("Corte Masculino", BigDecimal.valueOf(25.00), 30, "Corte tradicional");

        // Vincular serviço ao profissional
    factory.saveProfissionalServico(ctx.profissional, ctx.servico);

        // Escolher um horário futuro livre (próxima segunda-feira às 10:00)
    ctx.horarioSelecionado = factory.proximaData(DayOfWeek.MONDAY, 10, 0);

        // Verificar que o horário está realmente livre
    assertFalse(agendamentoService.temConflitoDeHorario(ctx.horarioSelecionado, ctx.profissional.getId()),
            "O horário selecionado deveria estar livre");
    }

    @Given("que informo as informações essenciais para o agendamento")
    public void que_informo_as_informações_essenciais_para_o_agendamento() {
        ctx.agendamentoRequest = new CriarAgendamentoRequest(
            ctx.horarioSelecionado,
            ctx.cliente.getId(),
            ctx.profissional.getId(),
            ctx.servico.getId(),
            "Agendamento de teste"
        );
    }

    @When("solicito a criação do agendamento em horário livre")
    public void solicito_a_criação_do_agendamento_em_horário_livre() {
        try {
            ctx.agendamentoResponse = agendamentoService.criarAgendamento(ctx.agendamentoRequest);
            ctx.exception = null;
        } catch (Exception e) {
            ctx.exception = e;
            ctx.agendamentoResponse = null;
        }
    }

    @Then("exibe a mensagem: {string}")
    public void exibe_a_mensagem(String mensagemEsperada) {
        if (ctx.exception != null) {
            assertTrue(ctx.exception.getMessage() != null && ctx.exception.getMessage().contains(mensagemEsperada),
                () -> "Mensagem esperada deveria conter: '" + mensagemEsperada + "' mas foi: " + ctx.exception.getMessage());
        } else {
            // Se não houve exceção, verificar se o agendamento foi criado com sucesso
            assertNotNull(ctx.agendamentoResponse, "O agendamento deveria ter sido criado com sucesso");
            assertNotNull(ctx.agendamentoResponse.getCliente(), "Cliente deveria estar presente no response");
            assertNotNull(ctx.agendamentoResponse.getProfissional(), "Profissional deveria estar presente no response");
            assertNotNull(ctx.agendamentoResponse.getServico(), "Serviço deveria estar presente no response");

            assertEquals(ctx.cliente.getId(), ctx.agendamentoResponse.getCliente().getId(),
                "O ID do cliente no agendamento deveria corresponder");
            assertEquals(ctx.profissional.getId(), ctx.agendamentoResponse.getProfissional().getId(),
                "O ID do profissional no agendamento deveria corresponder");
            assertEquals(ctx.servico.getId(), ctx.agendamentoResponse.getServico().getId(),
                "O ID do serviço no agendamento deveria corresponder");
        }
    }
}
