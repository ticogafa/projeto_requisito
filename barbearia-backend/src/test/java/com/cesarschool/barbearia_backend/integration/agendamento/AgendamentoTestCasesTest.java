package com.cesarschool.barbearia_backend.integration.agendamento;

import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.CriarAgendamentoRequest;
import com.cesarschool.barbearia_backend.common.enums.DiaSemana;
import com.cesarschool.barbearia_backend.common.enums.StatusAgendamento;
import com.cesarschool.barbearia_backend.config.FixedClockTestConfig;
import com.cesarschool.barbearia_backend.helper.TestEntityFactory;
import com.cesarschool.barbearia_backend.marketing.model.Cliente;
import com.cesarschool.barbearia_backend.profissionais.model.Profissional;
import com.cesarschool.barbearia_backend.profissionais.model.ServicoOferecido;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.Matchers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration(classes = { FixedClockTestConfig.class })
@Transactional
class AgendamentoTestCasesTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;
    @Autowired
    TestEntityFactory factory;

    @Autowired
    private Clock clock;
    
    private Profissional prof;
    private Cliente cliente;
    private ServicoOferecido servico;

    private final String BASE_URL = "/api/agendamentos/";

    @BeforeEach
    void setupTest() {
        prof = factory.saveProfissionalComJornada(
                "João Barbeiro",
                "joao@email.com",
                "53604042801",
                "1234567890",
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                DiaSemana.SEGUNDA,
                DiaSemana.TERCA,
                DiaSemana.QUARTA,
                DiaSemana.QUINTA,
                DiaSemana.SEXTA);

        cliente = factory.saveCliente(
                "Miguel Batista",
                "miguel@email.com",
                "02973067405",
                "0123456789");

        servico = factory.saveServico("Corte de Cabelo", new BigDecimal("30.00"), 30, "Corte legal");

        factory.saveProfissionalServico(prof, servico);
    }

    private String asJson(Object o) throws Exception {
        return om.writeValueAsString(o);
    }

    @Test
    void devecriarAgendamentoComSucessoQuandoHorarioEstiverLivreEDentroDaJornada() throws Exception {
        // Given: que escolho um horário futuro livre para o profissional
        // And: que informo cliente, profissional, serviço e data/hora
        var dataHora = factory.proximaData(DiaSemana.SEGUNDA.tDayOfWeek(), 10, 0);
        var payload = new CriarAgendamentoRequest(
                dataHora,
                cliente.getId(),
                prof.getId(),
                servico.getId(),
                "Teste de agendamento");

        // When: solicito a criação do agendamento
        var result = mvc.perform(post(BASE_URL + "criar-agendamento")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(payload)))
        
        // Then: o sistema responde sucesso (HTTP 2xx)
        // And: retorna o agendamento criado com um ID preenchido
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.profissionalNome").value("João Barbeiro"))
                .andReturn();

        System.out.println("Response: " + result.getResponse().getContentAsString());
    }

    @Test
    void deveFalharCriacaoQuandoJaExisteAgendamentoNoMesmoHorario() throws Exception {
        // Given: que já existe um agendamento para "João Barbeiro" no mesmo horário
        var slot = factory.proximaData(DayOfWeek.MONDAY, 10, 0);
        factory.saveAgendamento(
                cliente,
                prof,
                servico,
                slot,
                StatusAgendamento.PENDENTE,
                "Observações"    
                );

        // And: que tento criar outro agendamento nesse horário com os mesmos Givens essenciais
        var payload = new CriarAgendamentoRequest(
                slot,
                cliente.getId(),
                prof.getId(),
                servico.getId(),
                "conflict");

        // When: solicito a criação do novo agendamento
        var result = mvc.perform(post(BASE_URL + "criar-agendamento")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(payload)))
                
                // Then: o sistema rejeita a operação
                // And: exibe a mensagem: "Já existe um agendamento com João Barbeiro no horário especificado."
                .andExpect(status().isConflict())
                .andExpect(
                        jsonPath("$.message")
                                .value(Matchers.containsStringIgnoringCase("já existe um agendamento")));
        System.out.println("RESULT!: " + result);
    }

    @Test
    void deveFalharCriacaoQuandoProfissionalIndisponivelForaDaJornada() throws Exception {
        // Given: que escolho um horário futuro às 02:00 fora da jornada do profissional
        var fora = factory.proximaData(DayOfWeek.MONDAY, 2, 0); // 02:00

        // And: que informo cliente, profissional, serviço e data/hora
        var payload = new CriarAgendamentoRequest(
                fora,
                cliente.getId(),
                prof.getId(),
                servico.getId(),
                "Observações do agendamento");

        // When: solicito a criação do agendamento
        // Then: o sistema rejeita a operação
        // And: exibe a mensagem: "João Barbeiro não está disponível no horário solicitado."
        mvc.perform(post(BASE_URL + "criar-agendamento")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(payload)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(Matchers.containsString("não está disponível")));
    }

    @Test
    void deveCancelarAgendamentoComSucessoQuandoFaltamMaisDeDuasHoras() throws Exception {
        // Given: que existe um agendamento marcado para ocorrer em mais de 2 horas
        var horario = LocalDate.now(clock).atTime(14, 0);
        System.out.println("🕒 Horário atual (Clock): " + LocalDateTime.now(clock));
        System.out.println("📅 Horário do agendamento: " + horario);
        System.out.println("⏰ Diferença em horas: " + java.time.Duration.between(LocalDateTime.now(clock), horario).toHours());
        
        // And: que o status atual do agendamento é "PENDENTE"
        var ag = factory.saveAgendamento(
            cliente, 
            prof, 
            servico, 
            horario,
            StatusAgendamento.PENDENTE,
            "observações"
            );

        // When: solicito o cancelamento deste agendamento
        // Then: o sistema responde sucesso (HTTP 2xx)
        // And: o status do agendamento passa a ser "CANCELADO"
        var result = mvc.perform(patch(BASE_URL + "cancelar-agendamento/" + ag.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.status").value("CANCELADO"));
    
        System.out.println("THE RESULT: " + result);
        }

    @Test
    void deveFalharCancelamentoQuandoFaltamMenosDeDuasHoras() throws Exception {
        // Given: que existe um agendamento marcado para ocorrer em menos de 2 horas
        var horario = LocalDate.now(clock).atTime(10, 30); // agora (09:00) + 1h30 => < 2h
        
        // And: que o status atual do agendamento é "PENDENTE"
        var ag = factory.saveAgendamento(
                cliente,
                prof,
                servico, 
                horario,
                StatusAgendamento.PENDENTE,
                "observações"
                );

        // When: solicito o cancelamento deste agendamento
        // Then: o sistema rejeita a operação
        // And: exibe a mensagem: "Não é permitido cancelar agendamentos com menos de 2 horas de antecedência."
        mvc.perform(patch(BASE_URL + "cancelar-agendamento/" + ag.getId()))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(Matchers.containsStringIgnoringCase("Não é permitido cancelar")));
    }

    @Test
    void deveListarHorariosDisponiveisComSucesso() throws Exception {
        // Given: que há pelo menos um profissional disponível no horário solicitado
        // And: que informo uma data válida e um serviço existente
        var data = factory
            .proximaData(DayOfWeek.MONDAY, 0, 0)
            .toLocalDate()
            .toString();
                    
        // When: solicito a listagem de horários disponíveis
        // Then: o sistema responde sucesso (HTTP 2xx)
        // And: retorna uma lista de horários disponíveis
        mvc.perform(get(BASE_URL + "horarios-disponiveis")
                .param("data", data)
                .param("servicoId", servico.getId().toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(Matchers.greaterThan(0)));
    }

    @Test
    void deveListarProfissionaisDisponiveisComSucesso() throws Exception {
        // Given: que não informei um profissional explicitamente
        // And: que há pelo menos um profissional disponível no horário solicitado
        // And: que informo cliente, serviço e data/hora
        var data = factory
            .proximaData(DayOfWeek.MONDAY, 0, 0)
            .toLocalDate()
            .toString();
        
        // When: solicito a listagem de profissionais disponíveis
        // Then: o sistema responde sucesso (HTTP 2xx)
        // And: retorna pelo menos um profissional disponível
        mvc.perform(get(BASE_URL + "profissionais-disponiveis")
                .param("data", data)
                .param("horario", "10:00")
                .param("servicoId", servico.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Barbeiro"));
    }

}
