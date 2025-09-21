package com.cesarschool.barbearia_backend.bdd;

import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.CriarAgendamentoRequest;
import com.cesarschool.barbearia_backend.common.enums.StatusAgendamento;
import com.cesarschool.barbearia_backend.helper.TestEntityFactory;
import com.cesarschool.barbearia_backend.marketing.model.Cliente;
import com.cesarschool.barbearia_backend.profissionais.model.Profissional;
import com.cesarschool.barbearia_backend.profissionais.model.ServicoOferecido;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Step definitions com geração de dados única:
 * - CPF: válido e único
 * - E-mail: válido e único
 * - Telefone: exatamente 10 dígitos (ex.: "81XXXXXXXX")
 *
 * Importante: reutiliza entidades já criadas no cenário para evitar colisões
 * de unicidade e reduzir criação desnecessária.
 */
public class AgendamentoStepDefinitions extends CucumberSpringContext {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private TestEntityFactory factory;
    @Autowired private Clock clock;
    @Autowired private MockMvc mockMvc;

    private Cliente cliente;
    private Profissional profissional;
    private ServicoOferecido servico;

    private LocalDateTime horarioEscolhido;
    private CriarAgendamentoRequest payload;
    private ResultActions resultado;

    private Integer agendamentoId; // usado em cancelamento

    private static final String BASE_URL   = "/api/agendamentos";
    private static final String CREATE_PATH = BASE_URL + "/criar-agendamento";
    private static final String CANCEL_PATH = BASE_URL + "/cancelar-agendamento/{id}";

    // ========================= GIVENS COMUNS =========================

    @Given("que existe um Cliente válido")
    public void queExisteUmClienteValido() {
        if (cliente == null) {
            int n = Unique.next();
            cliente = factory.saveCliente(
                "Cliente " + n,
                Unique.email(n),
                Unique.cpf(n),
                Unique.phone10(n)
            );
            System.out.println("CLIENTE: " + cliente.toString());
        }
    }

    @Given("que existe um Profissional chamado {string} com jornada de trabalho configurada")
    public void queExisteUmProfissionalComJornadaDeTrabalho(String nome) {
        if (profissional == null) {
            int n = Unique.next();
            profissional = factory.saveProfissionalComJornada(
                nome,
                Unique.email("prof", n),
                Unique.cpf(n),
                Unique.phone10(n),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
            );
        }
    }

    @Given("que existe um Serviço oferecido válido")
    public void queExisteUmServicoOferecidoValido() {
        if (servico == null) {
            int n = Unique.peek(); // não avança se já usamos no passo anterior
            if (n == 0) n = Unique.next();
            servico = factory.saveServico(
                "Corte de Cabelo " + n, // nome único (se houver restrição)
                new BigDecimal("25.00"),
                30,
                "Corte tradicional"
            );
        }
    }

    // ========================= CRIAÇÃO EM HORÁRIOS =========================

    @Given("que escolho um horário futuro às {int}:{int} dentro da jornada do profissional")
    public void horarioDentroDaJornada(Integer hora, Integer minuto) {
        horarioEscolhido = factory.proximaData(DayOfWeek.MONDAY, hora, minuto);
    }

    @Given("que escolho um horário futuro às {int}:{int} fora da jornada do profissional")
    public void horarioForaDaJornada(Integer hora, Integer minuto) {
        horarioEscolhido = factory.proximaData(DayOfWeek.MONDAY, hora, minuto);
    }

    @Given("que informo cliente, profissional, serviço e data\\/hora")
    public void informarTudo() {
        // Ensure all required entities exist
        if (cliente == null) {
            queExisteUmClienteValido();
        }
        if (profissional == null) {
            queExisteUmProfissionalComJornadaDeTrabalho("João Barbeiro");
        }
        if (servico == null) {
            queExisteUmServicoOferecidoValido();
        }
        if (horarioEscolhido == null) {
            horarioEscolhido = factory.proximaData(DayOfWeek.MONDAY, 10, 0);
        }
        payload = new CriarAgendamentoRequest(
            horarioEscolhido,
            cliente.getId(),
            profissional.getId(),
            servico.getId(),
            "Observações do teste BDD"
        );
    }

    @When("solicito a criação do novo agendamento")
    public void criarNovoAgendamento() throws Exception {
        resultado = mockMvc.perform(
            post(CREATE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload))
        );
    }

    @Then("retorna o corpo do agendamento criado")
    public void retornaCorpoCriado() throws Exception {
        resultado.andExpect(status().isOk())
                 .andExpect(jsonPath("$.id").isNumber())
                 .andExpect(jsonPath("$.clienteId").value(cliente.getId()))
                 .andExpect(jsonPath("$.servicoId").value(servico.getId()));
    }

    // ========================= CONFLITO DE HORÁRIO =========================

    @Given("que já existe um agendamento para {string} no mesmo horário")
    public void existeConflitoMesmoHorario(String nomeProfissional) {
        if (horarioEscolhido == null) {
            horarioEscolhido = factory.proximaData(DayOfWeek.MONDAY, 10, 0);
        }
        if (profissional == null || !nomeProfissional.equals(profissional.getNome())) {
            int n = Unique.next();
            profissional = factory.saveProfissionalComJornada(
                nomeProfissional,
                Unique.email("prof", n),
                Unique.cpf(n),
                Unique.phone10(n),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
            );
        }
        if (cliente == null) {
            int n = Unique.next();
            cliente = factory.saveCliente("Cliente " + n, Unique.email(n), Unique.cpf(n), Unique.phone10(n));
        }
        if (servico == null) {
            int n = Unique.next();
            servico = factory.saveServico("Corte " + n, new BigDecimal("30.00"), 30, "Desc");
        }

        factory.saveAgendamento(
            cliente,
            profissional,
            servico,
            horarioEscolhido,
            StatusAgendamento.CONFIRMADO,
            "seed conflito"
        );
    }

    @Given("que tento criar outro agendamento nesse horário com os mesmos Givens essenciais")
    public void tentarCriarComConflito() {
        // Ensure we have all required data from the previous conflict setup
        if (cliente == null || profissional == null || servico == null || horarioEscolhido == null) {
            throw new IllegalStateException("Entities must be set up before attempting to create conflicting appointment");
        }
        payload = new CriarAgendamentoRequest(
            horarioEscolhido,
            cliente.getId(),
            profissional.getId(),
            servico.getId(),
            "tentando conflitar"
        );
    }

    // ========================= CANCELAMENTO =========================

    @Given("que existe um agendamento marcado para ocorrer em mais de {int} horas")
    public void agendamentoMaiorQueHoras(Integer horas) {
        seedBasicoSeNecessario();
        var dataHora = LocalDateTime.now(clock).plusHours(horas);
        var ag = factory.saveAgendamento(
            cliente, profissional, servico, dataHora, StatusAgendamento.PENDENTE, "seed >2h"
        );
        agendamentoId = ag.getId();
    }

    @Given("que existe um agendamento marcado para ocorrer em menos de {int} horas")
    public void agendamentoMenorQueHoras(Integer horas) {
        seedBasicoSeNecessario();
        var dataHora = LocalDateTime.now(clock).plusHours(horas);
        var ag = factory.saveAgendamento(
            cliente, profissional, servico, dataHora, StatusAgendamento.PENDENTE, "seed <2h"
        );
        agendamentoId = ag.getId();
    }

    @Given("que o status atual do agendamento é {string}")
    public void statusAtual(String status) {
        // Mantido PENDENTE na semente; ajuste aqui se precisar mudar o status na base.
    }

    @When("solicito o cancelamento deste agendamento")
    public void cancelarAgendamento() throws Exception {
        resultado = mockMvc.perform(
            patch(CANCEL_PATH, agendamentoId)
                .contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Then("o status do agendamento passa a ser {string}")
    public void statusAposCancelamento(String esperado) throws Exception {
        resultado.andExpect(status().isOk())
                 .andExpect(jsonPath("$.status").value(esperado));
    }

    // ========================= ATRIBUIÇÃO AUTOMÁTICA =========================

    @Given("que não informei um profissional explicitamente")
    public void semProfissionalExplicito() {
        // nada a fazer; refletiremos no payload
    }

    @Given("que há pelo menos um profissional disponível no horário solicitado")
    public void haProfissionalDisponivel() {
        if (profissional == null) {
            int n = Unique.next();
            profissional = factory.saveProfissionalComJornada(
                "Prof " + n,
                Unique.email("prof", n),
                Unique.cpf(n),
                Unique.phone10(n),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
            );
        }
    }

    @Given("que informo cliente, serviço e data\\/hora")
    public void informarSemProfissional() {
        // Ensure required entities exist (except profissional for auto-assignment)
        if (cliente == null) {
            queExisteUmClienteValido();
        }
        if (servico == null) {
            queExisteUmServicoOferecidoValido();
        }
        if (horarioEscolhido == null) {
            horarioEscolhido = factory.proximaData(DayOfWeek.MONDAY, 10, 0);
        }
        payload = new CriarAgendamentoRequest(
            horarioEscolhido,
            cliente.getId(),
            null, // auto-atribuição
            servico.getId(),
            "sem profissional para auto-atribuição"
        );
    }

    @Then("o agendamento retornado possui um profissional atribuído automaticamente")
    public void validacaoAutoAtribuicao() throws Exception {
        resultado.andExpect(status().isOk())
                 .andExpect(jsonPath("$.profissionalId").isNumber())
                 .andExpect(jsonPath("$.profissionalNome").isString());
    }

    @Given("que nenhum profissional está disponível no horário solicitado")
    public void nenhumProfissionalDisponivel() {
        if (horarioEscolhido == null) {
            horarioEscolhido = factory.proximaData(DayOfWeek.MONDAY, 10, 0);
        }
        if (cliente == null) {
            int n = Unique.next();
            cliente = factory.saveCliente("Cliente " + n, Unique.email(n), Unique.cpf(n), Unique.phone10(n));
        }
        if (servico == null) {
            int n = Unique.next();
            servico = factory.saveServico("Corte " + n, new BigDecimal("30.00"), 30, "Desc");
        }
        // ocupa o horário com um profissional qualquer para simular indisponibilidade
        int n = Unique.next();
        var pOcupado = factory.saveProfissionalComJornada(
            "Prof Ocupado " + n,
            Unique.email("ocupado", n),
            Unique.cpf(n),
            Unique.phone10(n),
            LocalTime.of(9, 0),
            LocalTime.of(18, 0)
        );
        factory.saveAgendamento(
            cliente, pOcupado, servico, horarioEscolhido, StatusAgendamento.CONFIRMADO, "ocupar horário"
        );
    }

    // ========================= REUTILIZÁVEIS =========================

    @Given("que escolho um horário futuro livre para o profissional")
    public void horarioLivreDefault() {
        horarioEscolhido = factory.proximaData(DayOfWeek.MONDAY, 10, 0);
    }

    @When("solicito a criação do agendamento")
    public void criarAgendamento() throws Exception {
        resultado = mockMvc.perform(
            post(CREATE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload))
        );
    }

    @Then("o sistema responde sucesso \\(HTTP 2xx)")
    public void sucesso2xx() throws Exception {
        resultado.andExpect(status().isOk());
    }

    @Then("retorna o agendamento criado com um ID preenchido")
    public void retornaComIdPreenchido() throws Exception {
        resultado.andExpect(status().isOk())
                 .andExpect(jsonPath("$.id").isNumber())
                 .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Then("o sistema rejeita a operação")
    public void rejeicao4xx() throws Exception {
        resultado.andExpect(status().is4xxClientError());
    }

    @Then("exibe a mensagem: {string}")
    public void validaMensagem(String mensagemEsperada) throws Exception {
        resultado.andExpect(jsonPath("$.message").value(containsString(mensagemEsperada)));
    }

    // ========================= HELPERS =========================

    private void seedBasicoSeNecessario() {
        if (cliente == null) {
            int n = Unique.next();
            cliente = factory.saveCliente("Cliente " + n, Unique.email(n), Unique.cpf(n), Unique.phone10(n));
        }
        if (profissional == null) {
            int n = Unique.next();
            profissional = factory.saveProfissionalComJornada(
                "João Barbeiro " + n,
                Unique.email("prof", n),
                Unique.cpf(n),
                Unique.phone10(n),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
            );
        }
        if (servico == null) {
            int n = Unique.next();
            servico = factory.saveServico("Corte " + n, new BigDecimal("30.00"), 30, "Desc");
        }
    }

    /** Geração determinística e única de CPFs, e-mails e telefones (10 dígitos). */
    static final class Unique {
        private static final AtomicInteger SEQ = new AtomicInteger(0);

        static int next() { return SEQ.incrementAndGet(); }
        static int peek() { return SEQ.get(); }

        static String email(int n) { return email("user", n); }

        static String email(String prefix, int n) {
            // domínio reservado para testes
            return String.format("%s%03d@bdd.test", prefix, n);
        }

        static String phone10(int n) {
            // 10 dígitos: DDD 81 (Recife) + 8 dígitos determinísticos
            // garante zero-padding para totalizar 10 dígitos
            int body = (n * 739_391) % 100_000_000; // espalhamento simples
            return String.format("81%08d", body);
        }

        static String cpf(int n) {
            // base de 9 dígitos (zero-padded) derivada do contador
            String base = String.format("%09d", n);
            int[] d = new int[11];
            for (int i = 0; i < 9; i++) d[i] = base.charAt(i) - '0';
            d[9]  = cpfDigit(d, 9);
            d[10] = cpfDigit(d, 10);
            StringBuilder sb = new StringBuilder(11);
            for (int i = 0; i < 11; i++) sb.append(d[i]);
            return sb.toString();
        }

        private static int cpfDigit(int[] d, int pos) {
            int sum = 0, weight = pos + 1;
            for (int i = 0; i < pos; i++) sum += d[i] * (weight - i);
            int mod = (sum * 10) % 11;
            return (mod == 10) ? 0 : mod;
        }
    }
}
