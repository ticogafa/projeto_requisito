package com.cesarschool.cucumber.agendamento;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.cesarschool.barbearia.dominio.compartilhado.enums.TipoUsuario;
import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoServico;
import com.cesarschool.barbearia.dominio.principal.agendamento.StatusAgendamento;
import com.cesarschool.barbearia.dominio.principal.agendamento.UsuarioSolicitante;
import com.cesarschool.barbearia.dominio.principal.cliente.Cliente;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalRepositorio;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalServico;
import com.cesarschool.cucumber.agendamento.infraestrutura.AgendamentoConflitoRepositorio;
import com.cesarschool.cucumber.agendamento.infraestrutura.AgendamentoFactory;
import com.cesarschool.cucumber.agendamento.infraestrutura.AgendamentoMockRepositorio;
import com.cesarschool.cucumber.agendamento.infraestrutura.ClienteFactory;
import com.cesarschool.cucumber.agendamento.infraestrutura.ProfissionalMockRepositorio;
import com.cesarschool.cucumber.agendamento.infraestrutura.ProfissionalSemDisponivelRepositorio;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AgendamentoTest {

private LocalDateTime horario;
private Agendamento agendamento;
private Agendamento agendamentoSalvo;
private UsuarioSolicitante clienteSolicitante;
private ProfissionalRepositorio repositorioProfissional = new ProfissionalMockRepositorio();
private ProfissionalServico profissionalServico = new ProfissionalServico(repositorioProfissional);
private AgendamentoRepositorio repositorio = new AgendamentoMockRepositorio();
boolean lancou = false;
Cliente cliente;

private AgendamentoServico servico = new AgendamentoServico(repositorio, profissionalServico);
private StatusAgendamento statusAgendamento;

// --------------------------------------------------------------------------
// Cenário: Criar agendamento em horário livre (sucesso)
// --------------------------------------------------------------------------

@Given("que o cliente selecionou um horário futuro livre para o profissional")
public void clienteSelecionaHorarioFuturoLivre() {
    // Garante que o horário está dentro do horário comercial (8h-18h)
    LocalDateTime agora = LocalDateTime.now();
    if (agora.getHour() < 8) {
        horario = agora.withHour(10).withMinute(0).plusDays(0);
    } else if (agora.getHour() >= 16) {
        horario = agora.withHour(10).withMinute(0).plusDays(1);
    } else {
        horario = agora.plusHours(2);
    }
}

@When("o cliente envia a solicitação de criação do agendamento")
public void clienteEnviaSolicitacaoDeCriacao() {
    agendamento = AgendamentoFactory.criarParaHorario(horario);
    agendamentoSalvo = servico.criar(agendamento, 30);
}

@Then("o sistema confirma a criação do agendamento com sucesso")
public void sistemaConfirmaCriacaoComSucesso() {
    assertNotNull("Agendamento não deve ser nulo", agendamentoSalvo);
    assertEquals(StatusAgendamento.PENDENTE, agendamentoSalvo.getStatus());
    assertTrue(agendamentoSalvo.getDataHora().isAfter(LocalDateTime.now()));
}

// --------------------------------------------------------------------------
// Cenário: Impedir criação de agendamento quando horário está ocupado
// --------------------------------------------------------------------------

@Given("que já existe um agendamento ativo para o profissional {string} no mesmo horário")
public void existeAgendamentoAtivoMesmoHorario(String nomeProfissional) {
    repositorio = new AgendamentoConflitoRepositorio(); // mock configurado para conflito
    servico = new AgendamentoServico(repositorio, profissionalServico);
    // Garante horário dentro do horário comercial
    LocalDateTime agora = LocalDateTime.now();
    if (agora.getHour() < 8) {
        horario = agora.withHour(10).withMinute(0).plusDays(0);
    } else if (agora.getHour() >= 16) {
        horario = agora.withHour(10).withMinute(0).plusDays(1);
    } else {
        horario = agora.plusHours(2);
    }
}

@When("o cliente tenta criar outro agendamento para o mesmo horário")
public void clienteTentaCriarOutroAgendamentoMesmoHorario() {
    agendamento = AgendamentoFactory.criarParaHorario(horario);
    lancou = false;
    try {
        servico.criar(agendamento, 30);
    } catch (IllegalStateException e) {
        lancou = true;
    }
}

@Then("o sistema deve recusar a criação por conflito de horário")
public void sistemaRecusaCriacaoPorConflito() {
    assertTrue("Deveria recusar a criação por conflito de horário", lancou);
}

// --------------------------------------------------------------------------
// Cenário: Bloquear agendamento fora da jornada
// --------------------------------------------------------------------------

@Given("Dado que o sistema funciona das 8h às 18h")
public void dadoQueOSistemaFuncionaDas8hÀs18h() {
    horario = LocalDateTime.now().withHour(7).withMinute(0).withSecond(0).withNano(0);
    repositorio = new AgendamentoMockRepositorio();
    servico = new AgendamentoServico(repositorio, profissionalServico);
}

@When("quando cliente solicita um horário fora da jornada")
public void quandoClienteSolicitaUmHorárioForaDaJornada() {
    agendamento = AgendamentoFactory.criarParaHorario(horario);
    lancou = false;
    try {
        servico.criar(agendamento, 30);
    } catch (IllegalStateException e) {
        lancou = true;
    }
}
@Then("o sistema deve negar o agendamento por estar não estar entre 8h e 18h")
public void oSistemaDeveNegarOAgendamentoPorEstarNãoEstarEntre8hE18h() {
    assertTrue("Deveria recusar a criação por estar fora do horário permitido", lancou);
}

// --------------------------------------------------------------------------
// Cenário: Impedir cancelamento com menos de 2 horas
// --------------------------------------------------------------------------

@Given("que existe um agendamento marcado para ocorrer em menos de duas horas e com status {string}")
public void existeAgendamentoMenosDeDuasHoras(String status) {
    statusAgendamento = StatusAgendamento.valueOf(status);
    repositorio = new AgendamentoMockRepositorio();
    servico = new AgendamentoServico(repositorio, profissionalServico);
    cliente = ClienteFactory.criarPadrao();
    clienteSolicitante = new UsuarioSolicitante(TipoUsuario.CLIENTE, cliente.getId());
    
    // Cria e salva o agendamento no repositório
    agendamento = AgendamentoFactory.criarComStatus(statusAgendamento);
    agendamentoSalvo = repositorio.salvar(agendamento);
}

@When("o cliente solicita o cancelamento desse agendamento")
public void clienteSolicitaCancelamentoAgendamento() {
    lancou = false;
    try {

        servico.cancelar(agendamentoSalvo.getId(), clienteSolicitante);
    } catch (IllegalStateException e) {
        lancou = true;
    }
}

@Then("o sistema deve recusar o cancelamento por descumprir o prazo mínimo")
public void sistemaRecusaCancelamentoPrazoMinimo() {
    assertTrue("Deveria recusar o cancelamento por descumprir o prazo mínimo", lancou);
}

// --------------------------------------------------------------------------
// Cenário: Atribuição automática de profissional (simples)
// --------------------------------------------------------------------------

@Given("que o cliente não informou nenhum profissional ao criar o agendamento")
public void clienteNaoInformouProfissional() {
    // Garante que o horário está dentro do horário comercial (8h-18h)
    LocalDateTime agora = LocalDateTime.now();
    if (agora.getHour() < 8) {
        horario = agora.withHour(10).withMinute(0).plusDays(0);
    } else if (agora.getHour() >= 16) {
        horario = agora.withHour(10).withMinute(0).plusDays(1);
    } else {
        horario = agora.plusHours(2);
    }
    agendamento = AgendamentoFactory.criarParaHorario(horario);
    agendamento.setProfissional(null);
}

@When("a solicitação de agendamento é processada pelo sistema")
public void solicitacaoAgendamentoProcessada() {
    lancou = false;
    try {
        agendamento.setProfissional(null); // garante que não há profissional
        agendamentoSalvo = servico.criar(agendamento, 30);
    } catch (IllegalStateException e) {
        lancou = true;
    }
}

@Then("o sistema deve criar o agendamento atribuindo o primeiro profissional disponível")
public void sistemaAtribuiPrimeiroProfissionalDisponivel() {
    assertTrue("Deveria haver um profissional disponível para o horário", !lancou);
}
// --------------------------------------------------------------------------
// Cenário: Atribuição automática de profissional (com mensagem)
// --------------------------------------------------------------------------

@Given("que o cliente não informou nenhum profissional ao criar o agendamento e não existie profissional disponível no horário")
public void queOClienteNãoInformouNenhumProfissionalAoCriarOAgendamentoENãoExistieProfissionalDisponívelNoHorário() {
    // Configura repositório que não retorna profissional disponível
    repositorioProfissional = new ProfissionalSemDisponivelRepositorio();
    profissionalServico = new ProfissionalServico(repositorioProfissional);
    servico = new AgendamentoServico(repositorio, profissionalServico);
    
    // Garante horário dentro do horário comercial
    LocalDateTime agora = LocalDateTime.now();
    if (agora.getHour() < 8) {
        horario = agora.withHour(10).withMinute(0).plusDays(0);
    } else if (agora.getHour() >= 16) {
        horario = agora.withHour(10).withMinute(0).plusDays(1);
    } else {
        horario = agora.plusHours(2);
    }
    
    agendamento = AgendamentoFactory.criarParaHorario(horario);
    agendamento.setProfissional(null); // garante que não há profissional
}

@Then("o sistema deve recusar a criação do agendamento informando que não há profissionais disponíveis")
public void oSistemaDeveRecusarACriaçãoDoAgendamentoInformandoQueNãoHáProfissionaisDisponíveis() {
    assertTrue("Deveria recusar a criação por falta de profissionais disponíveis", lancou);
}
}