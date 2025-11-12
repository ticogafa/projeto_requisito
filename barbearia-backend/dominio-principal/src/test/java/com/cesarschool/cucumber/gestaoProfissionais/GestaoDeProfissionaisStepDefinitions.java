package com.cesarschool.cucumber.gestaoProfissionais;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;
import com.cesarschool.barbearia.dominio.principal.profissional.Agenda;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalServico;
import com.cesarschool.barbearia.dominio.principal.profissional.Senioridade;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class GestaoDeProfissionaisStepDefinitions {

    private ProfissionalMockRepositorio repositorioMock;
    private ProfissionalServico profissionalServico;
    private Profissional profissionalAtual;
    private Exception excecaoCapturada;
    private Map<String, Profissional> profissionalCache = new HashMap<>();
    private int indiceProfissional;
    
    private String tipoUsuarioLogado = "ADMIN"; 

    private static Exception excecaoCompartilhada;
    
    public static void setExcecaoCompartilhada(Exception excecao) {
        excecaoCompartilhada = excecao;
    }

    @Before
    public void setup() {
        long timestamp = System.currentTimeMillis();
        long nanoTime = System.nanoTime();
        this.indiceProfissional = (int) ((timestamp + nanoTime) % 100000);
        
        this.repositorioMock = new ProfissionalMockRepositorio();
        this.profissionalServico = new ProfissionalServico(repositorioMock);
        this.profissionalAtual = null;
        this.excecaoCapturada = null;
        this.profissionalCache.clear();
        this.foiCadastradoComNivelEspecifico = false;
        
        this.tipoUsuarioLogado = "ADMIN";
    }
    
    private Cpf gerarCpfValido(int indice) {
        String cpfBase = String.format("%09d", indice);
        String d1 = calcularDigito(cpfBase, 10);
        String d2 = calcularDigito(cpfBase + d1, 11);
        return new Cpf(cpfBase + d1 + d2);
    }
    
    private String calcularDigito(String str, int peso) {
        int soma = 0;
        for (int i = 0; i < str.length(); i++) {
            soma += (str.charAt(i) - '0') * peso--;
        }
        int resto = soma % 11;
        return String.valueOf(resto < 2 ? 0 : 11 - resto);
    }
    
    private Profissional criarProfissionalGenerico(String nome) {
        int indice = this.indiceProfissional++;
        Cpf cpf = gerarCpfValido(indice);
        // Remove acentos e espaços para criar um email válido
        String nomeEmail = nome.replaceAll("\\s+", "")
                               .toLowerCase()
                               .replaceAll("ã", "a")
                               .replaceAll("õ", "o")
                               .replaceAll("á", "a")
                               .replaceAll("é", "e")
                               .replaceAll("í", "i")
                               .replaceAll("ó", "o")
                               .replaceAll("ú", "u")
                               .replaceAll("ç", "c");
        Email email = new Email(nomeEmail + indice + "@barbearia.com");
        Telefone telefone = new Telefone("819" + String.format("%08d", indice));
        Profissional novo = new Profissional(nome, email, cpf, telefone);
        return novo; 
    }

    @Given("que eu cadastro um novo profissional chamado {string}")
    public void queEuCadastroUmNovoProfissionalChamado(String nome) {
        profissionalAtual = criarProfissionalGenerico(nome);
        profissionalCache.put(nome, profissionalAtual);
    }

    @When("eu visualizo a agenda de {string}")
    public void euVisualizoAAgendaDe(String nome) {
        profissionalAtual = profissionalCache.get(nome);
    }

    @Then("a disponibilidade deve estar configurada para {int} horas por dia")
    public void aDisponibilidadeDeveEstarConfiguradaParaHorasPorDia(Integer horasEsperadas) {
        int jornadaHoras = profissionalAtual.getAgenda().calcularJornadaHoras();
        Assertions.assertEquals(horasEsperadas.intValue(), jornadaHoras);
    }
   
    
    @Given("que sou um usuário não administrador")
    public void queSouUmUsuárioNãoAdministrador() {
        this.tipoUsuarioLogado = "CLIENTE";
        Profissional p = criarProfissionalGenerico("ProfissionalAlvoJornada");
        this.profissionalAtual = profissionalServico.registrarNovo(p, Senioridade.JUNIOR);
    }

    @When("eu tento configurar a jornada de trabalho de um profissional")
    public void euTentoConfigurarAJornadaDeTrabalhoDeUmProfissional() {
        try {
            Assertions.assertNotNull(profissionalAtual, "O profissional não foi criado no step Given");
            Assertions.assertNotNull(profissionalAtual.getId(), "O profissional não foi salvo e não tem ID");
            Agenda jornadaFicticia = new Agenda(); 
            profissionalServico.configurarJornada(
                profissionalAtual.getId(), 
                jornadaFicticia, 
                this.tipoUsuarioLogado
            );

        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }
    

    @Given("que sou um administrador logado")
    public void queSouUmAdministradorLogado() {
        this.tipoUsuarioLogado = "ADMIN";
    }

    private boolean foiCadastradoComNivelEspecifico = false;

    @When("eu cadastro um novo profissional com nível {string}")
    public void euCadastroUmNovoProfissionalComNível(String nivelSenioridade) {
        Profissional novoProfissional = criarProfissionalGenerico("Profissional Nivel");
        Senioridade senioridade = Senioridade.valueOf(nivelSenioridade.toUpperCase());

        try {
            profissionalAtual = profissionalServico.registrarNovo(novoProfissional, senioridade);
            profissionalCache.put(profissionalAtual.getNome(), profissionalAtual);
            foiCadastradoComNivelEspecifico = true;
        } catch (Exception e) {
            excecaoCapturada = e; 
        }
    }

    @Then("o sistema responde com sucesso")
    public void oSistemaRespondeComSucesso() {
        Assertions.assertNull(excecaoCapturada, "Nenhuma exceção deveria ter sido lançada.");
        Assertions.assertNotNull(profissionalAtual, "Profissional não deveria ser nulo.");
        Assertions.assertNotNull(profissionalAtual.getId(), "O ID do profissional não deveria ser nulo.");
        
        if (foiCadastradoComNivelEspecifico) {
            Assertions.assertNotEquals(Senioridade.JUNIOR, profissionalAtual.getSenioridade(), "A senioridade JUNIOR deveria ter sido sobrescrita.");
        }
    }
    
    @When("eu tento cadastrar um novo profissional com nível {string}")
    public void euTentoCadastrarUmNovoProfissionalComNível(String nivelSenioridade) {
        Profissional novoProfissional = criarProfissionalGenerico("Profissional Invalido");

        try {
            Senioridade senioridade = Senioridade.valueOf(nivelSenioridade.toUpperCase());
            profissionalServico.registrarNovo(novoProfissional, senioridade);
        } catch (IllegalArgumentException e) {
            excecaoCapturada = e; 
        }
    }
    

    @Given("que o profissional {string} possui o serviço {string} com agendamentos ativos")
    public void queOProfissionalPossuiOServiçoComAgendamentosAtivos(String nomeProfissional, String nomeServico) {
        repositorioMock.simularAgendamentoAtivo(nomeServico, true);
        repositorioMock.salvarAssociacaoServico(nomeProfissional, nomeServico);
    }
    
    @When("eu tento remover o serviço {string}")
    public void euTentoRemoverOServiço(String nomeServico, String nomeProfissional) {
        try {
            profissionalServico.removerServico(nomeProfissional, nomeServico);
        } catch (IllegalStateException e) {
            excecaoCapturada = e;
        }
    }

    @Then("o sistema vai rejeitar a operação")
    public void oSistemaVaiRejeitarAOperação() {
        Exception excecaoParaTeste = excecaoCapturada != null ? excecaoCapturada : excecaoCompartilhada;
        
        Assertions.assertNotNull(excecaoParaTeste, "Era esperada uma exceção para rejeitar a operação.");
        Assertions.assertTrue(excecaoParaTeste instanceof IllegalArgumentException || excecaoParaTeste instanceof IllegalStateException, 
            "A exceção esperada era de validação ou estado ilegal.");
    }
}
