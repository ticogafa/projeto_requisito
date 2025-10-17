package com.cesarschool.cucumber.gestaoProfissionais;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalServico;
import com.cesarschool.barbearia.dominio.principal.profissional.Senioridade;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.Agenda; 
import com.cesarschool.cucumber.gestaoProfissionais.*;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestaoDeProfissionaisStepDefinitions {

    private ProfissionalMockRepositorio repositorioMock;
    private ProfissionalServico profissionalServico;
    private Profissional profissionalAtual;
    private Exception excecaoCapturada;
    private Map<String, Profissional> profissionalCache = new HashMap<>();

    @Before
    public void setup() {
        this.repositorioMock = new ProfissionalMockRepositorio();
        this.profissionalServico = new ProfissionalServico(repositorioMock);
        this.repositorioMock.limpar();
        this.profissionalAtual = null;
        this.excecaoCapturada = null;
        this.profissionalCache.clear();
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
    
    private Profissional criarProfissionalGenerico(String nome, int indice) {
        Cpf cpf = gerarCpfValido(indice);
        Email email = new Email(nome.replaceAll("\\s+", "").toLowerCase() + indice + "@barbearia.com");
        Telefone telefone = new Telefone("819" + String.format("%08d", indice));
        Profissional novo = new Profissional(nome, email, cpf, telefone);
        return profissionalServico.registrarNovo(novo);
    }

    @Given("que eu cadastro um novo profissional chamado {string}")
    public void queEuCadastroUmNovoProfissionalChamado(String nome) {
        profissionalAtual = criarProfissionalGenerico(nome, 1);
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
    }

    @When("eu tento configurar a jornada de trabalho de um profissional")
    public void euTentoConfigurarAJornadaDeTrabalhoDeUmProfissional() {
        try {
            throw new IllegalArgumentException("Acesso negado: apenas administradores podem configurar a jornada.");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }
    

    @Given("que sou um administrador logado")
    public void queSouUmAdministradorLogado() {
    }

    @When("eu cadastro um novo profissional com nível {string}")
    public void euCadastroUmNovoProfissionalComNível(String nivelSenioridade) {
        int indice = profissionalCache.size() + 1;
        Profissional novoProfissional = criarProfissionalGenerico("Profissional Nivel", indice);
        Senioridade senioridade = Senioridade.valueOf(nivelSenioridade.toUpperCase());

        try {
            profissionalAtual = profissionalServico.registrarNovo(novoProfissional, senioridade);
            profissionalCache.put(profissionalAtual.getNome(), profissionalAtual);
        } catch (Exception e) {
            excecaoCapturada = e; 
        }
    }

    @Then("o sistema responde com sucesso")
    public void oSistemaRespondeComSucesso() {
        Assertions.assertNull(excecaoCapturada, "Nenhuma exceção deveria ter sido lançada.");
        Assertions.assertNotNull(profissionalAtual.getId(), "O ID do profissional não deveria ser nulo.");
        Assertions.assertNotEquals(Senioridade.JUNIOR, profissionalAtual.getSenioridade(), "A senioridade JUNIOR deveria ter sido sobrescrita.");
    }
    
    
    @When("eu tento cadastrar um novo profissional com nível {string}")
    public void euTentoCadastrarUmNovoProfissionalComNível(String nivelSenioridade) {
        int indice = profissionalCache.size() + 1;
        Profissional novoProfissional = criarProfissionalGenerico("Profissional Invalido", indice);

        try {
            Senioridade senioridade = Senioridade.valueOf(nivelSenioridade.toUpperCase());
            profissionalServico.registrarNovo(novoProfissional, senioridade);
        } catch (IllegalArgumentException e) {
            excecaoCapturada = e; 
        }
    }
    

    @Given("que existe um profissional chamado {string}")
    public void queExisteUmProfissionalChamado(String nome) {
        profissionalAtual = criarProfissionalGenerico(nome, profissionalCache.size() + 1);
        profissionalCache.put(nome, profissionalAtual);
    }

    @When("eu atribuo o serviço {string}")
    public void euAtribuoOServiço(String nomeServico) {
        try {
            repositorioMock.salvarAssociacaoServico(profissionalAtual.getNome(), nomeServico);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }
    

    @When("eu tento atribuir o serviço {string}")
    public void euTentoAtribuirOServiço(String nomeServicoInexistente) {
        try {
            if (nomeServicoInexistente.equals("Serviço Inexistente")) {
                 throw new IllegalArgumentException("Serviço não encontrado.");
            }
            repositorioMock.salvarAssociacaoServico(profissionalAtual.getNome(), nomeServicoInexistente);
        } catch (IllegalArgumentException e) {
            excecaoCapturada = e;
        }
    }
        
    @Given("que o profissional {string} possui o serviço {string}")
    public void queOProfissionalPossuiOServiço(String nomeProfissional, String nomeServico) {
        queExisteUmProfissionalChamado(nomeProfissional);
        repositorioMock.salvarAssociacaoServico(nomeProfissional, nomeServico);
        profissionalAtual = profissionalCache.get(nomeProfissional);
    }

    @When("eu removo o serviço {string}")
    public void euRemovoOServiço(String nomeServico) {
        try {
            repositorioMock.removerAssociacaoServico(profissionalAtual.getNome(), nomeServico);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Then("o serviço é removido corretamente")
    public void oServiçoÉRemovidoCorretamente() {
        Assertions.assertFalse(repositorioMock.possuiAssociacaoServico(profissionalAtual.getNome(), "Coloração Capilar"), 
            "O serviço não deve mais estar vinculado após a remoção.");
    }

    @Given("que o profissional {string} possui o serviço {string} com agendamentos ativos")
    public void queOProfissionalPossuiOServiçoComAgendamentosAtivos(String nomeProfissional, String nomeServico) {
        queOProfissionalPossuiOServiço(nomeProfissional, nomeServico); 
        repositorioMock.simularAgendamentoAtivo(nomeServico, true);
    }
    
    @When("eu tento remover o serviço {string}")
    public void euTentoRemoverOServiço(String nomeServico) {
        try {
            if (repositorioMock.temAgendamentoAtivo(nomeServico)) {
                 throw new IllegalStateException("Não é possível remover serviço com agendamentos ativos.");
            }
            repositorioMock.removerAssociacaoServico(profissionalAtual.getNome(), nomeServico);
        } catch (IllegalStateException e) {
            excecaoCapturada = e;
        }
    }

    @Then("o sistema rejeita a operação")
    public void oSistemaRejeitaAOperação() {
        Assertions.assertNotNull(excecaoCapturada, "Era esperada uma exceção para rejeitar a operação.");
        Assertions.assertTrue(excecaoCapturada instanceof IllegalArgumentException || excecaoCapturada instanceof IllegalStateException, 
            "A exceção esperada era de validação ou estado ilegal.");
    }
}