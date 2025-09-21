package com.cesarschool.barbearia_backend.agendamento;

import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.CriarAgendamentoRequest;
import com.cesarschool.barbearia_backend.common.enums.DiaSemana;
import com.cesarschool.barbearia_backend.common.enums.StatusAgendamento;
import com.cesarschool.barbearia_backend.helper.FixedClockTestConfig;
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
    void criarAgendamento_sucesso_quandoHorarioLivreEDentroDaJornada() throws Exception {
        var dataHora = factory.proximaData(DiaSemana.SEGUNDA.tDayOfWeek(), 10, 0);

        var payload = new CriarAgendamentoRequest(
                dataHora,
                cliente.getId(),
                prof.getId(),
                servico.getId(),
                "Teste de agendamento");

        var result = mvc.perform(post(BASE_URL + "criar-agendamento")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.profissionalNome").value("João Barbeiro"))
                .andReturn();

        System.out.println("Response: " + result.getResponse().getContentAsString());
    }

    @Test
    void criarAgendamento_falha_quandoConflitoDeHorario() throws Exception {
        var slot = factory.proximaData(DayOfWeek.MONDAY, 10, 0);

        // ocupa o slot previamente
        factory.saveAgendamento(
                cliente,
                prof,
                servico,
                slot,
                StatusAgendamento.PENDENTE,
                "Observações"    
                );

        var payload = new CriarAgendamentoRequest(
                slot,
                cliente.getId(),
                prof.getId(),
                servico.getId(),
                "conflict");

        var result = mvc.perform(post(BASE_URL + "criar-agendamento")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(payload)))
                .andExpect(status().isConflict())
                .andExpect(
                        jsonPath("$.message")
                                .value(Matchers.containsStringIgnoringCase("já existe um agendamento")));
        System.out.println("RESULT!: " + result);
    }

    @Test
    void criarAgendamento_falha_foraDaJornada() throws Exception {
        var fora = factory.proximaData(DayOfWeek.MONDAY, 2, 0); // 02:00

        var payload = new CriarAgendamentoRequest(
                fora,
                cliente.getId(),
                prof.getId(),
                servico.getId(),
                "Observações do agendamento");

        mvc.perform(post(BASE_URL + "criar-agendamento")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(payload)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(Matchers.containsString("não está disponível")));
    }

    @Test
    void cancelarAgendamento_sucesso_quandoFaltamMaisDeDuasHoras() throws Exception {
        var horario = LocalDate.now(clock).atTime(14, 0);
        
        var ag = factory.saveAgendamento(
            cliente, 
            prof, 
            servico, 
            horario,
            StatusAgendamento.PENDENTE,
            "observações"
            );

        var result = mvc.perform(patch(BASE_URL + "cancelar-agendamento/" + ag.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.status").value("CANCELADO"));
    
        System.out.println("THE RESULT: " + result);
        }

    @Test
    void cancelarAgendamento_falha_quandoFaltamMenosDeDuasHoras() throws Exception {
        var horario = LocalDate.now(clock).atTime(10, 30); // agora (09:00) + 1h30 => < 2h
        var ag = factory.saveAgendamento(
                cliente,
                prof,
                servico, 
                horario,
                StatusAgendamento.PENDENTE,
                "observações"
                );

        mvc.perform(patch(BASE_URL + "cancelar-agendamento/" + ag.getId()))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(Matchers.containsStringIgnoringCase("Não é permitido cancelar")));
    }

    @Test
    void listarHorariosDisponiveis_sucesso() throws Exception {
        var data = factory
            .proximaData(DayOfWeek.MONDAY, 0, 0)
            .toLocalDate()
            .toString();
                    
        mvc.perform(get(BASE_URL + "horarios-disponiveis")
                .param("data", data)
                .param("servicoId", servico.getId().toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(Matchers.greaterThan(0)));
    }

    @Test
    void listarProfissionaisDisponiveis_sucesso() throws Exception {
        // assume que endpoint checa os que já têm o serviço associado (se houver tal
        // vínculo no seu modelo)

        var data = factory
            .proximaData(DayOfWeek.MONDAY, 0, 0)
            .toLocalDate()
            .toString();
        
        mvc.perform(get(BASE_URL + "profissionais-disponiveis")
                .param("data", data)
                .param("horario", "10:00")
                .param("servicoId", servico.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Barbeiro"));
    }

}
