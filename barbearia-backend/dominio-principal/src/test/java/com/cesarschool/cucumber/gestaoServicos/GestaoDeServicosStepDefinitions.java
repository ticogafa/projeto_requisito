package com.cesarschool.cucumber.gestaoServicos;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;

import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoServico;

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
    private Exception excecaoCapturada; 
    private Map<String, ServicoOferecido> servicosCache = new HashMap<>();

    private Integer duracaoAtual;
    private BigDecimal precoAtual;
    private String nomeProfissionalAtual;
    private String nomeServicoAtual;


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
        this.duracaoAtual = null;
        this.precoAtual = null;
        this.nomeProfissionalAtual = null;
        this.nomeServicoAtual = null;
    }

    @Given("que não existe um serviço chamado {string}")
    public void que_nao_existe_um_servico_chamado(String nomeServico) {
        ServicoOferecido servicoEncontrado = repositorioMock.buscarPorNome(nomeServico);
        Assertions.assertNull(servicoEncontrado);
    }

    @When("eu crio um novo serviço com o nome {string}")
    public void eu_crio_um_novo_servico_com_o_nome(String nomeServico) {
        servicoAInserir = new ServicoOferecido(
            nomeServico,
            new BigDecimal("100.00"),
            "Descricao muito criativa",
            101 
        );
        servicoCriado = servicoOferecidoServico.registrar(servicoAInserir);
    }

    @Then("o serviço é criado com sucesso")
    public void o_servico_e_criado_com_sucesso() {
        Assertions.assertNull(excecaoCapturada, "Nenhuma exceção era esperada");
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

    @Given("que existe um serviço chamado {string}")
    public void que_existe_um_servico_chamado(String nomeServico) {
        ServicoOferecido servicoParaAtualizar = new ServicoOferecido(
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
        this.duracaoAtual = Integer.parseInt(duracao);
        this.precoAtual = new BigDecimal(preco);
        
        try {
            servicoExistente = servicoOferecidoServico.atualizarDuracao(servicoExistente.getId().getValor(), this.duracaoAtual);
            servicoExistente = servicoOferecidoServico.atualizarPreco(servicoExistente.getId().getValor(), this.precoAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Then("o sistema salva as alterações com sucesso")
    public void o_sistema_salva_as_alteracoes_com_sucesso() {
        Assertions.assertNull(excecaoCapturada, "Nenhuma exceção era esperada");
        Assertions.assertNotNull(servicoExistente);
        ServicoOferecido servicoPersistido = repositorioMock.buscarPorIdOptional(
            servicoExistente.getId().getValor()).orElse(null);
        Assertions.assertNotNull(servicoPersistido);
        Assertions.assertEquals(this.duracaoAtual, servicoPersistido.getDuracaoMinutos());
        Assertions.assertEquals(0, this.precoAtual.compareTo(servicoPersistido.getPreco()));
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

    @Then("o sistema irá rejeitar a operação")
    public void oSistemaIráRejeitarAOperação() {     
        Assertions.assertNotNull(excecaoCapturada, "Uma exceção era esperada, mas nada foi capturado.");
        Assertions.assertTrue(excecaoCapturada instanceof IllegalArgumentException);
    }

    @Given("que existe um serviço chamado {string} ativo")
    public void que_existe_um_serviço_chamado_ativo(String nomeServico) {
        servicoExistente = new ServicoOferecido(nomeServico, new BigDecimal("80.00"), "Serviço", 60);
        servicoExistente = repositorioMock.salvar(servicoExistente);
        servicosCache.put(nomeServico, servicoExistente);
        
        boolean estaAtivo = servicoOferecidoServico.isAtivo(servicoExistente.getId().getValor());
        Assertions.assertTrue(estaAtivo, "O serviço deveria ter sido criado como ativo.");
    }

    @When("eu desativo o serviço por motivo de {string}")
    public void eu_desativo_o_serviço_por_motivo_de(String motivo) {
        try {
            servicoExistente = servicoOferecidoServico.desativar(servicoExistente.getId().getValor(), motivo);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Then("o serviço aparece como {string} na lista de opções para agendamento")
    public void o_serviço_aparece_como_na_lista_de_opções_para_agendamento(String statusEsperado) {
        Assertions.assertNull(excecaoCapturada, "Nenhuma exceção era esperada ao desativar.");
        boolean estaAtivo = servicoOferecidoServico.isAtivo(servicoExistente.getId().getValor());
        if (statusEsperado.equalsIgnoreCase("Inativo")) {
            Assertions.assertFalse(estaAtivo, "O serviço deveria estar Inativo.");
        } else if (statusEsperado.equalsIgnoreCase("Ativo")) {
            Assertions.assertTrue(estaAtivo, "O serviço deveria estar Ativo.");
        } else {
            Assertions.fail("Status esperado desconhecido: " + statusEsperado);
        }
        
        ServicoOferecido servicoPersistido = repositorioMock.buscarPorId(servicoExistente.getId().getValor());
        if (!estaAtivo) {
            Assertions.assertNotNull(servicoPersistido.getMotivoInatividade());
        }
    }

    @Given("que o serviço {string} está inativo por {string}")
    public void que_o_serviço_está_inativo_por(String nomeServico, String motivo) {
        servicoExistente = new ServicoOferecido(nomeServico, new BigDecimal("80.00"), "Serviço", 60);
        servicoExistente = repositorioMock.salvar(servicoExistente);
        servicoExistente = servicoOferecidoServico.desativar(servicoExistente.getId().getValor(), motivo);
        servicosCache.put(nomeServico, servicoExistente);

        boolean estaAtivo = servicoOferecidoServico.isAtivo(servicoExistente.getId().getValor());
        Assertions.assertFalse(estaAtivo, "O serviço deveria estar inativo para este cenário.");
    }

    @When("o cliente acessa as opções de agendamento")
    public void o_cliente_acessa_as_opções_de_agendamento() {
        
    }

    @Then("o sistema não exibe o serviço {string} na lista")
    public void o_sistema_nao_exibe_o_serviço_na_lista(String nomeServicoInativo) {
        ServicoOferecido servico = repositorioMock.buscarPorNome(nomeServicoInativo);
        Assertions.assertNotNull(servico, "Serviço deveria existir no mock.");
        boolean estaAtivo = servicoOferecidoServico.isAtivo(servico.getId().getValor());
        Assertions.assertFalse(estaAtivo, "O serviço inativo não deveria ser exibido (assumindo que a lista é filtrada).");
    }

    @Given("que existe o profissional {string} qualificado para {string}")
    public void que_existe_o_profissional_qualificado_para(String nomeProfissional, String nomeServico) {
        servicoExistente = servicosCache.computeIfAbsent(nomeServico, n -> {
            ServicoOferecido s = new ServicoOferecido(n, new BigDecimal("80.00"), "Serviço", 60);
            return repositorioMock.salvar(s);
        });
        repositorioMock.salvarAssociacao(nomeServico, nomeProfissional);
        servicosCache.put(nomeProfissional, null); 
        Assertions.assertTrue(repositorioMock.estaQualificado(nomeServico, nomeProfissional));
    }

    @When("eu associo o serviço {string} ao profissional {string}")
    public void eu_associo_o_serviço_ao_profissional(String nomeServico, String nomeProfissional) {
        this.nomeServicoAtual = nomeServico;
        this.nomeProfissionalAtual = nomeProfissional;
        try {
            servicoOferecidoServico.associarProfissional(nomeServico, nomeProfissional);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Then("o sistema salva a associação com sucesso")
    public void o_sistema_salva_a_associação_com_sucesso() {
        Assertions.assertTrue(repositorioMock.estaQualificado(this.nomeServicoAtual, this.nomeProfissionalAtual));
        Assertions.assertNull(excecaoCapturada, "Nenhuma exceção era esperada.");
    }

    @Given("que existe o profissional {string} sem qualificação para {string}")
    public void que_existe_o_profissional_sem_qualificacao_para(String nomeProfissional, String nomeServico) {
        servicoExistente = servicosCache.computeIfAbsent(nomeServico, n -> {
            ServicoOferecido s = new ServicoOferecido(n, new BigDecimal("80.00"), "Serviço", 60);
            return repositorioMock.salvar(s);
        });
        Assertions.assertFalse(repositorioMock.estaQualificado(nomeServico, nomeProfissional));
        servicosCache.put(nomeProfissional, null); 
    }

    @When("eu tento associar o serviço {string} ao profissional {string}")
    public void eu_tento_associar_o_serviço_ao_profissional(String nomeServico, String nomeProfissional) {
        try {
            servicoOferecidoServico.associarProfissional(nomeServico, nomeProfissional);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

}
