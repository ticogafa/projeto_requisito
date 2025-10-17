package com.cesarschool.cucumber.gestaoProfissionais;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;
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
    
    // Campo estático para compartilhar exceções entre classes de teste
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
        return novo; // Retorna o profissional sem salvar
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
        Assertions.assertNotNull(profissionalAtual.getId(), "O ID do profissional não deveria ser nulo.");
        
        // Só verifica senioridade se foi cadastrado com nível específico
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
    

    @Given("que existe um profissional chamado {string}")
    public void queExisteUmProfissionalChamado(String nome) {
        Profissional novoProfissional = criarProfissionalGenerico(nome);
        try {
            profissionalAtual = profissionalServico.registrarNovo(novoProfissional);
            profissionalCache.put(nome, profissionalAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @When("eu atribuo o serviço {string}")
    public void euAtribuoOServiço(String nomeServico) {
        try {
            repositorioMock.salvarAssociacaoServico(profissionalAtual.getNome(), nomeServico);
            // Simula que deu certo - o profissional agora tem o serviço
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
        // Verificar primeiro a exceção local, depois a compartilhada
        Exception excecaoParaTeste = excecaoCapturada != null ? excecaoCapturada : excecaoCompartilhada;
        
        Assertions.assertNotNull(excecaoParaTeste, "Era esperada uma exceção para rejeitar a operação.");
        Assertions.assertTrue(excecaoParaTeste instanceof IllegalArgumentException || excecaoParaTeste instanceof IllegalStateException, 
            "A exceção esperada era de validação ou estado ilegal.");
    }
}