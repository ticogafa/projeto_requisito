package com.cesarschool.cucumber.profissional;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.cesarschool.barbearia.aplicacao.profissional.AtualizarJornadaComando;
import com.cesarschool.barbearia.aplicacao.profissional.JornadaResumo;
import com.cesarschool.barbearia.aplicacao.profissional.ProfissionalServicoAplicacao;
import com.cesarschool.barbearia.dominio.compartilhado.enums.DiaSemana;
import com.cesarschool.cucumber.gestaoProfissionais.ProfissionalMockRepositorio;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class JornadaTest {

    private ProfissionalMockRepositorio repositorio = new ProfissionalMockRepositorio();
    private ProfissionalServicoAplicacao servico = new ProfissionalServicoAplicacao(repositorio);
    private List<JornadaResumo> jornadas = new ArrayList<>();
    private Integer profissionalId = 1;
    private boolean erroValidacao = false;

    @Given("que o profissional {string} deseja atualizar sua jornada")
    public void queOProfissionalDesejaAtualizarSuaJornada(String nome) {
        jornadas.clear();
        erroValidacao = false;
    }

    @Given("define que na {string} trabalha das {string} às {string}")
    public void defineQueNaTrabalhaDasÀs(String dia, String inicio, String fim) {
        jornadas.add(JornadaResumo.builder()
            .diaSemana(DiaSemana.valueOf(dia))
            .horaInicio(LocalTime.parse(inicio))
            .horaFim(LocalTime.parse(fim))
            .ativo(true)
            .build());
    }

    @Given("define um intervalo das {string} às {string}")
    public void defineUmIntervaloDasÀs(String inicio, String fim) {
        // Assume interval applies to the last added day
        JornadaResumo last = jornadas.get(jornadas.size() - 1);
        last.setIntervaloInicio(LocalTime.parse(inicio));
        last.setIntervaloFim(LocalTime.parse(fim));
    }

    @When("o profissional envia a solicitação de atualização de jornada")
    public void oProfissionalEnviaASolicitaçãoDeAtualizaçãoDeJornada() {
        AtualizarJornadaComando comando = new AtualizarJornadaComando(profissionalId, jornadas);
        try {
            servico.atualizarJornada(comando);
        } catch (IllegalArgumentException e) {
            erroValidacao = true;
        }
    }

    @Then("o sistema confirma a atualização da jornada com sucesso")
    public void oSistemaConfirmaAAtualizaçãoDaJornadaComSucesso() {
        assertTrue(!erroValidacao, "Deveria ter atualizado com sucesso");
    }

    @Then("o sistema deve recusar a atualização por horário inválido")
    public void oSistemaDeveRecusarAAtualizaçãoPorHorárioInválido() {
        assertTrue(erroValidacao, "Deveria ter recusado por horário inválido");
    }

    @Then("o sistema deve recusar a atualização por intervalo inválido")
    public void oSistemaDeveRecusarAAtualizaçãoPorIntervaloInválido() {
        assertTrue(erroValidacao, "Deveria ter recusado por intervalo inválido");
    }
}
