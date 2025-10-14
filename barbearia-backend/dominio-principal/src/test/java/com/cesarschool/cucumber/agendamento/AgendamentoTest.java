package com.cesarschool.cucumber.agendamento;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;
import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoId;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.agendamento.StatusAgendamento;
import com.cesarschool.barbearia.dominio.principal.cliente.Cliente;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

class AgendamentoMockRepositorio implements AgendamentoRepositorio{
    @Override
    public Agendamento salvar(Agendamento agendamento) {
        return new Agendamento(
        LocalDateTime.now().plusHours(2),
        new ClienteId(1),
        new ProfissionalId(1),
        new ServicoOferecidoId(1),
        "Nenhuma observação"
    );
    }
    @Override
    public List<Agendamento> buscarPorCliente(ClienteId clienteId) { return new ArrayList<>(); }
    @Override
    public Optional<Agendamento> buscarPorId(AgendamentoId id) { return Optional.empty(); }
    @Override
    public List<Agendamento> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) { return new ArrayList<>(); }
    @Override
    public List<Agendamento> buscarPorProfissional(ProfissionalId profissionalId) { return new ArrayList<>();}
    @Override
    public List<Agendamento> buscarPorStatus(StatusAgendamento status) { return new ArrayList<>(); }
    @Override
    public boolean existeAgendamentoNoPeriodo(ProfissionalId profissionalId, LocalDateTime dataHora, int duracaoMinutos) { return false; }
    @Override
    public List<Agendamento> listarTodos() { return new ArrayList<>(); }
    @Override
    public void remover(AgendamentoId id) {}
}

public class AgendamentoTest {
    
    private Cliente cliente;
    private LocalDateTime horario;
    private Agendamento agendamento;
    private Agendamento agendamentoSalvo;
    private final AgendamentoRepositorio repositorio = new AgendamentoMockRepositorio();

@Given("que escolho um horário futuro livre para o profissional")
public void que_escolho_um_horário_futuro_livre_para_o_profissional() {
    // Write code here that turns the phrase above into concrete actions
    horario = LocalDateTime.now().plusHours(2);

}
@Given("que informo as informações essenciais")
public void que_informo_as_informações_essenciais() {
    cliente = new Cliente(
        new ClienteId(1),
        "João Silva",
        new Email("joao@email.com"),
        new Cpf("53604042801"),
        new Telefone("81999999999")
    );
        
    agendamento = new Agendamento(
        horario,
        cliente.getId(),
        new ProfissionalId(1),
        new ServicoOferecidoId(1),
        "Nenhuma observação"
    );
}
@When("solicito a criação do agendamento")
public void solicito_a_criação_do_agendamento() {
    agendamentoSalvo = repositorio.salvar(agendamento);
}
@Then("o sistema responde sucesso")
public void o_sistema_responde_sucesso() {
    Validacoes.validarObjetoObrigatorio(agendamentoSalvo, "agendamento");
}
}

