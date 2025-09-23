package com.cesarschool.barbearia_backend.bdd;

import org.springframework.beans.factory.annotation.Autowired;

import com.cesarschool.barbearia_backend.helper.TestEntityFactory;
import com.cesarschool.barbearia_backend.agendamento.service.AgendamentoService;
import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.CriarAgendamentoRequest;
// imports específicos de modelos removidos (usamos apenas via contexto)
import com.cesarschool.barbearia_backend.common.enums.DiaSemana;

import io.cucumber.java.en.Given;

import java.math.BigDecimal;
import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step definitions específicos para o cenário de conflito de horário em gestão de agendamentos.
 * Reaproveita a infraestrutura de testes já existente sem duplicar a lógica de criação básica
 * (a lógica principal de criação e validação de sucesso permanece em AgendamentoStepDefinitions).
 */
public class GestaoAgendamentoStepDefinitions extends CucumberSpringContext {

    @Autowired private TestEntityFactory factory;
    @Autowired private AgendamentoService agendamentoService;
    @Autowired private AgendamentoContext ctx;

    /**
     * Garante a existência prévia de um agendamento para o mesmo profissional e horário.
     * Depois NÃO cria novo request ainda — isso será feito pelo passo "que informo as informações essenciais..."
     */
    @Given("que já existe um agendamento para o mesmo profissional e horário selecionado")
    public void que_ja_existe_um_agendamento_para_o_mesmo_profissional_e_horario_selecionado() {
        // 1. Monta dados base idênticos ao passo existente de horário livre
    ctx.cliente = factory.saveCliente("Cliente Conflito", "cliente.conflito@email.com", "11111111111", "11999990000");

    ctx.profissional = factory.saveProfissionalComJornada(
            "Profissional Conflito",
            "prof.conflito@barbearia.com",
            "22222222222",
            "11888887777",
            java.time.LocalTime.of(8, 0),
            java.time.LocalTime.of(18, 0),
            DiaSemana.SEGUNDA,
            DiaSemana.TERCA,
            DiaSemana.QUARTA,
            DiaSemana.QUINTA,
            DiaSemana.SEXTA
        );

    ctx.servico = factory.saveServico("Corte Conflito", BigDecimal.valueOf(30.00), 30, "Corte para conflito");
    factory.saveProfissionalServico(ctx.profissional, ctx.servico);

        // Escolhe um horário futuro determinístico (próxima segunda às 10h) — mesmo padrão do outro step
    ctx.horarioSelecionado = factory.proximaData(DayOfWeek.MONDAY, 10, 0);

        // 2. Cria um agendamento inicial (baseline) para gerar o conflito
        CriarAgendamentoRequest primeiro = new CriarAgendamentoRequest(
            ctx.horarioSelecionado,
            ctx.cliente.getId(),
            ctx.profissional.getId(),
            ctx.servico.getId(),
            "Agendamento base para conflito"
        );

        try {
            agendamentoService.criarAgendamento(primeiro);
        } catch (Exception e) {
            fail("Falha inesperada ao criar agendamento base para conflito: " + e.getMessage());
        }

        // 3. Prepara (mas não envia ainda) o segundo request que deverá conflitar
        ctx.agendamentoRequest = new CriarAgendamentoRequest(
            ctx.horarioSelecionado,
            ctx.cliente.getId(),
            ctx.profissional.getId(),
            ctx.servico.getId(),
            "Segundo agendamento que deve conflitar"
        );
    }
}
