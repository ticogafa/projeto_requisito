package com.cesarschool.cucumber.gestaoServicos;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoServico;
import com.cesarschool.cucumber.servico.ServicoOferecidoMockRepositorio;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class GestaoDeServicosStepDefinitions {

    private ServicoOferecidoMockRepositorio repositorioMock;
    private ServicoOferecidoServico servicoOferecidoServico;
    private ServicoOferecido servicoAInserir;
    private ServicoOferecido servicoCriado;
    private ServicoOferecido servicoExistente; 
    private final ProfissionalId profissionalIdTeste = new ProfissionalId(1);
    private Exception excecaoCapturada; 
    private Map<String, ServicoOferecido> servicosCache = new HashMap<>();

    @Before
    public void setup() {
        this.repositorioMock = new ServicoOferecidoMockRepositorio();
        this.servicoOferecidoServico = new ServicoOferecidoServico(repositorioMock);
        this.repositorioMock.limpar();
        this.servicoAInserir = null;
        this.servicoCriado = null;
        this.servicoExistente = null; 
        this.excecaoCapturada = null;
        this.servicosCache.clear();
    }

    @Given("que não existe um serviço chamado {string}")
    public void que_nao_existe_um_servico_chamado(String nomeServico) {
        ServicoOferecido servicoEncontrado = repositorioMock.buscarPorNome(nomeServico);
        Assertions.assertNull(servicoEncontrado);
    }

    @When("eu crio um novo serviço com o nome {string}")
    public void eu_crio_um_novo_servico_com_o_nome(String nomeServico) {
        servicoAInserir = new ServicoOferecido(
            profissionalIdTeste,
            nomeServico,
            new BigDecimal("100.00"),
            "Descricao muito criativa",
            101 
        );
        servicoCriado = servicoOferecidoServico.registrar(servicoAInserir);
    }

    @Then("o serviço é criado com sucesso")
    public void o_servico_e_criado_com_sucesso() {
        Assertions.assertNotNull(servicoCriado);
        Assertions.assertNotNull(servicoCriado.getId());
        ServicoOferecido servicoPersistido = repositorioMock.buscarPorIdOptional(
            servicoCriado.getId().getValor()).orElse(null);
        Assertions.assertNotNull(servicoPersistido);
        Assertions.assertEquals(servicoAInserir.getNome(), servicoPersistido.getNome());
    }
    
    @Given("que já existe um serviço chamado {string}")
    public void que_ja_existe_um_servico_chamado(String nomeServicoExistente) {
        ServicoOferecido servicoExistente = new ServicoOferecido(
            profissionalIdTeste,
            nomeServicoExistente,
            new BigDecimal("50.00"),
            "Serviço existente",
            60
        );
        repositorioMock.salvar(servicoExistente);
        Assertions.assertNotNull(repositorioMock.buscarPorNome(nomeServicoExistente));
    }

    @When("eu tento criar um novo serviço com o nome {string}")
    public void eu_tento_criar_um_novo_servico_com_o_nome(String nomeServicoDuplicado) {
        ServicoOferecido servicoDuplicado = new ServicoOferecido(
            profissionalIdTeste,
            nomeServicoDuplicado, 
            new BigDecimal("100.00"),
            "Descricao duplicada",
            101
        );
        try {
            servicoOferecidoServico.registrar(servicoDuplicado);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Then("o sistema salva a dependência corretamente")
    public void o_sistema_salva_a_dependencia_corretamente() {
        Assertions.assertNotNull(servicoCriado);
        ServicoOferecido principal = servicosCache.get("Corte"); 
        Assertions.assertEquals(principal.getId(), servicoCriado.getServicoPrincipalId());
        ServicoOferecido addOnPersistido = repositorioMock.buscarPorId(servicoCriado.getId().getValor());
        Assertions.assertEquals(principal.getId(), addOnPersistido.getServicoPrincipalId());
    }

    @Given("que existe um serviço chamado {string}")
    public void que_existe_um_servico_chamado(String nomeServico) {
        ServicoOferecido servicoParaAtualizar = new ServicoOferecido(
            profissionalIdTeste,
            nomeServico,
            new BigDecimal("50.00"),
            "Serviço para teste de atualização",
            30 
        );
        servicoExistente = repositorioMock.salvar(servicoParaAtualizar); 
        servicosCache.put(nomeServico, servicoExistente);
        Assertions.assertNotNull(servicoExistente.getId());
    }

    @When("eu altero a duração para {string} e o preço para {string}")
    public void eu_altero_a_duracao_para_e_o_preco_para(String duracao, String preco) {
        Integer novaDuracao = Integer.parseInt(duracao);
        BigDecimal novoPreco = new BigDecimal(preco);
        try {
            servicoExistente = servicoOferecidoServico.atualizarDuracao(servicoExistente.getId().getValor(), novaDuracao);
            servicoExistente = servicoOferecidoServico.atualizarPreco(servicoExistente.getId().getValor(), novoPreco);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Then("o sistema salva as alterações com sucesso")
    public void o_sistema_salva_as_alteracoes_com_sucesso() {
        Assertions.assertNotNull(servicoExistente);
        ServicoOferecido servicoPersistido = repositorioMock.buscarPorIdOptional(
            servicoExistente.getId().getValor()).orElse(null);
        Assertions.assertNotNull(servicoPersistido);
        Assertions.assertEquals(60, servicoPersistido.getDuracaoMinutos());
        Assertions.assertEquals(new BigDecimal("70.00"), servicoPersistido.getPreco());
    }

    @Given("que existe um serviço chamado {string} ativo")
    public void que_existe_um_serviço_chamado_ativo(String nomeServico) {
        servicoExistente = servicosCache.computeIfAbsent(nomeServico, n -> {
            ServicoOferecido s = new ServicoOferecido(profissionalIdTeste, n, new BigDecimal("80.00"), "Serviço", 60);
            return repositorioMock.salvar(s);
        });
        Assertions.assertTrue(servicoExistente.isAtivo());
    }

    @When("eu desativo o serviço por motivo de {string}")
    public void eu_desativo_o_serviço_por_motivo_de(String motivo) {
        try {
            servicoExistente = servicoOferecidoServico.desativarServico(servicoExistente.getId().getValor(), motivo);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Then("o serviço aparece como {string} na lista de opções para agendamento")
    public void o_serviço_aparece_como_na_lista_de_opções_para_agendamento(String statusEsperado) {
        Assertions.assertFalse(servicoExistente.isAtivo());
        List<ServicoOferecido> ativos = servicoOferecidoServico.listarServicosAtivos();
        boolean estaNaLista = ativos.stream().anyMatch(s -> s.getId().equals(servicoExistente.getId()));
        Assertions.assertFalse(estaNaLista);
    }

    @Given("que o serviço {string} está inativo por {string}")
    public void que_o_serviço_está_inativo_por(String nomeServico, String motivo) {
        servicoExistente = servicosCache.computeIfAbsent(nomeServico, n -> {
            ServicoOferecido s = new ServicoOferecido(profissionalIdTeste, n, new BigDecimal("80.00"), "Serviço", 60);
            return repositorioMock.salvar(s);
        });
        servicoExistente = servicoOferecidoServico.desativarServico(servicoExistente.getId().getValor(), motivo);
        Assertions.assertFalse(servicoExistente.isAtivo());
    }

    @When("o cliente acessa as opções de agendamento")
    public void o_cliente_acessa_as_opções_de_agendamento() {}

    @Then("o sistema não exibe o serviço {string} na lista")
    public void o_sistema_nao_exibe_o_serviço_na_lista(String nomeServicoInativo) {
        List<ServicoOferecido> ativos = servicoOferecidoServico.listarServicosAtivos();
        boolean estaNaLista = ativos.stream().anyMatch(s -> s.getNome().equals(nomeServicoInativo));
        Assertions.assertFalse(estaNaLista);
    }

    @When("eu tento alterar a duração para um valor negativo")
    public void eu_tento_alterar_a_duracao_para_um_valor_negativo() {
        Integer duracaoInvalida = -10;
        try {
            servicoOferecidoServico.atualizarDuracao(servicoExistente.getId().getValor(), duracaoInvalida);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Then("o sistema salva o intervalo corretamente")
    public void o_sistema_salva_o_intervalo_corretamente() {
        Assertions.assertNotNull(servicoExistente);
        ServicoOferecido servicoPersistido = repositorioMock.buscarPorIdOptional(
            servicoExistente.getId().getValor()).orElse(null);
        Assertions.assertNotNull(servicoPersistido);
        Assertions.assertEquals(10, servicoPersistido.getIntervaloLimpezaMinutos());
    }

    @Then("o sistema irá rejeitar a operação")
    public void oSistemaIráRejeitarAOperação() {     
        Assertions.assertNotNull(excecaoCapturada);
        Assertions.assertTrue(excecaoCapturada instanceof IllegalArgumentException);
    }
}
