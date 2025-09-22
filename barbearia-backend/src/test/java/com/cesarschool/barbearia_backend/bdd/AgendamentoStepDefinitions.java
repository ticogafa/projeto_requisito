package com.cesarschool.barbearia_backend.bdd;

import org.springframework.beans.factory.annotation.Autowired;

import com.cesarschool.barbearia_backend.helper.TestEntityFactory;
import com.cesarschool.barbearia_backend.agendamento.service.AgendamentoService;
import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.CriarAgendamentoRequest;
import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.AgendamentoResponse;
import com.cesarschool.barbearia_backend.marketing.model.Cliente;
import com.cesarschool.barbearia_backend.profissionais.model.Profissional;
import com.cesarschool.barbearia_backend.profissionais.model.ServicoOferecido;
import com.cesarschool.barbearia_backend.common.enums.DiaSemana;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


public class AgendamentoStepDefinitions extends CucumberSpringContext {

    @Autowired
    private TestEntityFactory factory;

    @Autowired
    private AgendamentoService agendamentoService;

    private Cliente cliente;
    private Profissional profissional;
    private ServicoOferecido servico;
    private LocalDateTime horarioSelecionado;
    private CriarAgendamentoRequest agendamentoRequest;
    private AgendamentoResponse agendamentoResponse;
    private Exception exception;

    @Given("que escolho um horário futuro livre para o profissional")
    public void que_escolho_um_horário_futuro_livre_para_o_profissional() {
        // Criar dados de teste usando a factory
        cliente = factory.saveCliente("João Silva", "joao@email.com", "12345678901", "11999999999");

        profissional = factory.saveProfissionalComJornada(
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

        servico = factory.saveServico("Corte Masculino", BigDecimal.valueOf(25.00), 30, "Corte tradicional");

        // Vincular serviço ao profissional
        factory.saveProfissionalServico(profissional, servico);

        // Escolher um horário futuro livre (próxima segunda-feira às 10:00)
        horarioSelecionado = factory.proximaData(DayOfWeek.MONDAY, 10, 0);

        // Verificar que o horário está realmente livre
        assertFalse(agendamentoService.temConflitoDeHorario(horarioSelecionado, profissional.getId()),
            "O horário selecionado deveria estar livre");
    }

    @Given("que informo as informações essenciais para o agendamento")
    public void que_informo_as_informações_essenciais_para_o_agendamento() {
        agendamentoRequest = new CriarAgendamentoRequest(
            horarioSelecionado,
            cliente.getId(),
            profissional.getId(),
            servico.getId(),
            "Agendamento de teste"
        );
    }

    @When("solicito a criação do agendamento em horário livre")
    public void solicito_a_criação_do_agendamento_em_horário_livre() {
        try {
            agendamentoResponse = agendamentoService.criarAgendamento(agendamentoRequest);
            exception = null;
        } catch (Exception e) {
            exception = e;
            agendamentoResponse = null;
        }
    }

    @Then("exibe a mensagem: {string}")
    public void exibe_a_mensagem(String mensagemEsperada) {
        if (exception != null) {
            assertEquals(mensagemEsperada, exception.getMessage(),
                "A mensagem de erro deveria ser: " + mensagemEsperada);
        } else {
            // Se não houve exceção, verificar se o agendamento foi criado com sucesso
            assertNotNull(agendamentoResponse, "O agendamento deveria ter sido criado com sucesso");
            assertEquals(cliente.getId(), agendamentoResponse.getClienteId(),
                "O ID do cliente no agendamento deveria corresponder");
            assertEquals(profissional.getId(), agendamentoResponse.getProfissionalId(),
                "O ID do profissional no agendamento deveria corresponder");
            assertEquals(servico.getId(), agendamentoResponse.getServicoId(),
                "O ID do serviço no agendamento deveria corresponder");
        }
    }
}
