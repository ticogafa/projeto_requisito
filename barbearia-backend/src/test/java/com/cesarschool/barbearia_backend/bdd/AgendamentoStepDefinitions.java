// package com.cesarschool.barbearia_backend.bdd;

// import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.CriarAgendamentoRequest;
// import com.cesarschool.barbearia_backend.common.enums.DiaSemana;
// import com.cesarschool.barbearia_backend.common.enums.StatusAgendamento;
// import com.cesarschool.barbearia_backend.helper.TestEntityFactory;
// import com.cesarschool.barbearia_backend.marketing.model.Cliente;
// import com.cesarschool.barbearia_backend.profissionais.model.Profissional;
// import com.cesarschool.barbearia_backend.profissionais.model.ServicoOferecido;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import io.cucumber.java.en.Given;
// import io.cucumber.java.en.When;
// import io.cucumber.java.en.Then;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.ResultActions;

// import java.math.BigDecimal;
// import java.time.Clock;
// import java.time.DayOfWeek;
// import java.time.LocalDateTime;
// import java.time.LocalTime;
// import java.util.concurrent.atomic.AtomicInteger;

// import static org.hamcrest.Matchers.containsString;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// /**
//  * Step definitions com geração de dados única:
//  * - CPF: válido e único
//  * - E-mail: válido e único
//  * - Telefone: exatamente 10 dígitos (ex.: "81XXXXXXXX")
//  *
//  * Importante: reutiliza entidades já criadas no cenário para evitar colisões
//  * de unicidade e reduzir criação desnecessária.
//  */
// public class AgendamentoStepDefinitions extends CucumberSpringContext {

//     @Autowired private ObjectMapper objectMapper;
//     @Autowired private TestEntityFactory factory;
//     @Autowired private Clock clock;
//     @Autowired private MockMvc mockMvc;

//     private Cliente cliente;
//     private Profissional profissional;
//     private ServicoOferecido servico;

//     private LocalDateTime horarioEscolhido;
//     private CriarAgendamentoRequest payload;
//     private ResultActions resultado;

//     private Integer agendamentoId; // usado em cancelamento

//     private static final String BASE_URL   = "/api/agendamentos";
//     private static final String CREATE_PATH = BASE_URL + "/criar-agendamento";
//     private static final String CANCEL_PATH = BASE_URL + "/cancelar-agendamento/{id}";

//     // ========================= GIVENS COMUNS =========================

// @Given("que escolho um horário futuro livre para o profissional")
// public void que_escolho_um_horário_futuro_livre_para_o_profissional() {
//     horarioEscolhido = factory.proximaData(DayOfWeek.MONDAY, 14, 0);
// }

// @Given("que informo cliente, profissional, serviço e data\\/hora")
// public void que_informo_cliente_profissional_serviço_e_data_hora() {
//         profissional = factory.saveProfissionalComJornada(
//                 "João Barbeiro",
//                 "joao@email.com",
//                 "53604042801",
//                 "1234567890",
//                 LocalTime.of(9, 0),
//                 LocalTime.of(18, 0),
//                 DiaSemana.SEGUNDA,
//                 DiaSemana.TERCA,
//                 DiaSemana.QUARTA,
//                 DiaSemana.QUINTA,
//                 DiaSemana.SEXTA);

//         cliente = factory.saveCliente(
//                 "Miguel Batista",
//                 "miguel@email.com",
//                 "02973067405",
//                 "0123456789");

//         servico = factory.saveServico("Corte de Cabelo", new BigDecimal("30.00"), 30, "Corte legal");

//         factory.saveProfissionalServico(profissional, servico);
// }

// @When("solicito a criação do agendamento")
// public void solicito_a_criação_do_agendamento() throws Exception{
//     resultado = mockMvc.perform(post(CREATE_PATH)
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(payload))
//             );
// }

// @Then("o sistema responde sucesso \\(HTTP 2xx)")
// public void o_sistema_responde_sucesso_http_2xx() throws Exception{
//     resultado.andExpect(
//         status().is2xxSuccessful()
//     );
// }
// @Then("retorna o agendamento criado com um ID preenchido")
// public void retorna_o_agendamento_criado_com_um_id_preenchido() throws Exception{
//     resultado.andExpect(
//         jsonPath("$.id").isNumber()
//     );
// }


// @Given("que já existe um agendamento para {string} no mesmo horário")
// public void que_já_existe_um_agendamento_para_no_mesmo_horário(String string) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Given("que tento criar outro agendamento nesse horário com os mesmos Givens essenciais")
// public void que_tento_criar_outro_agendamento_nesse_horário_com_os_mesmos_givens_essenciais() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @When("solicito a criação do novo agendamento")
// public void solicito_a_criação_do_novo_agendamento() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Then("o sistema rejeita a operação")
// public void o_sistema_rejeita_a_operação() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Then("exibe a mensagem: {string}")
// public void exibe_a_mensagem(String string) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }

// // ----------------------- other test case

// @Given("que escolho um horário futuro às {int}:{int} dentro da jornada do profissional")
// public void que_escolho_um_horário_futuro_às_dentro_da_jornada_do_profissional(Integer int1, Integer int2) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Then("retorna o corpo do agendamento criado")
// public void retorna_o_corpo_do_agendamento_criado() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }


// // ----------------------- other test case

// @Given("que escolho um horário futuro às {int}:{int} fora da jornada do profissional")
// public void que_escolho_um_horário_futuro_às_fora_da_jornada_do_profissional(Integer int1, Integer int2) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Then("o sistema rejeita a operação")
// public void o_sistema_rejeita_a_operação_() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Then("exibe a mensagem: {string}")
// public void exibe_a_mensagem_(String string) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }




// @Given("que existe um agendamento marcado para ocorrer em mais de {int} horas")
// public void que_existe_um_agendamento_marcado_para_ocorrer_em_mais_de_horas(Integer int1) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Given("que o status atual do agendamento é {string}")
// public void que_o_status_atual_do_agendamento_é(String string) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @When("solicito o cancelamento deste agendamento")
// public void solicito_o_cancelamento_deste_agendamento() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Then("o status do agendamento passa a ser {string}")
// public void o_status_do_agendamento_passa_a_ser(String string) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }



// @Given("que existe um agendamento marcado para ocorrer em menos de {int} horas")
// public void que_existe_um_agendamento_marcado_para_ocorrer_em_menos_de_horas(Integer int1) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Given("que o status atual do agendamento é {string}")
// public void que_o_status_atual_do_agendamento_é(String string) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @When("solicito o cancelamento deste agendamento")
// public void solicito_o_cancelamento_deste_agendamento() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Then("o sistema rejeita a operação")
// public void o_sistema_rejeita_a_operação() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Then("exibe a mensagem: {string}")
// public void exibe_a_mensagem(String string) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }



// @Given("que não informei um profissional explicitamente")
// public void que_não_informei_um_profissional_explicitamente() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Given("que há pelo menos um profissional disponível no horário solicitado")
// public void que_há_pelo_menos_um_profissional_disponível_no_horário_solicitado() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Given("que informo cliente, serviço e data\\/hora")
// public void que_informo_cliente_serviço_e_data_hora() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Then("o agendamento retornado possui um profissional atribuído automaticamente")
// public void o_agendamento_retornado_possui_um_profissional_atribuído_automaticamente() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }


// @Given("que não informei um profissional explicitamente")
// public void que_não_informei_um_profissional_explicitamente() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Given("que nenhum profissional está disponível no horário solicitado")
// public void que_nenhum_profissional_está_disponível_no_horário_solicitado() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Given("que informo cliente, serviço e data\\/hora")
// public void que_informo_cliente_serviço_e_data_hora() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Then("o sistema rejeita a operação")
// public void o_sistema_rejeita_a_operação() {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// @Then("exibe a mensagem: {string}")
// public void exibe_a_mensagem(String string) {
//     // Write code here that turns the phrase above into concrete actions
//     throw new io.cucumber.java.PendingException();
// }
// }
