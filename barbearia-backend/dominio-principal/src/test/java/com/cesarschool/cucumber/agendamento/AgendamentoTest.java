package com.cesarschool.cucumber.agendamento;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

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
    private final AgendamentoRepositorio repositorio = new AgendamentoMockRepositorio();
    private final AgendamentoServico servico = new AgendamentoServico(repositorio);

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
}