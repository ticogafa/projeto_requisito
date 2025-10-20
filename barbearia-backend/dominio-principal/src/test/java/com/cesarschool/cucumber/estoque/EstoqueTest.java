package com.cesarschool.cucumber.estoque;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class EstoqueTest {
    
    // ==================== ATRIBUTOS DE CONTROLE DE TESTE ====================
    
    /** Repositório mock para simular operações de banco de dados de estoque */
    private EstoqueMockRepositorio repositorio;
    
    /** Flag para indicar se a última operação foi executada com sucesso */
    private boolean operacaoSucesso;
    
    /** Mensagem de retorno da última operação executada */
    private String mensagemRetorno;
    
    /** Exceção capturada durante a execução de operações que falharam */
    private Exception excecaoLancada;
    
    /** Nome do produto sendo manipulado no teste atual */
    private String produtoAtual;
    
    /** Lista de produtos com estoque abaixo do mínimo (para relatórios) */
    private List<EstoqueMockRepositorio.Produto> produtosBaixo;
    
    /** Histórico de movimentações de estoque consultado */
    private List<EstoqueMockRepositorio.MovimentacaoEstoque> historicoConsultado;
    
    // ==================== CONFIGURAÇÃO DE TESTE ====================
    
    @Before
    public void setUp() {
        // Inicializa o repositório mock para simular o banco de dados
        repositorio = new EstoqueMockRepositorio();
        
        // Reset das variáveis de controle de estado
        operacaoSucesso = false;
        mensagemRetorno = "";
        excecaoLancada = null;
        produtoAtual = "";
        produtosBaixo = null;
        historicoConsultado = null;
        
        // Limpa todos os dados do repositório para começar com estado limpo
        repositorio.limparDados();
    }

    // ==================== STEP DEFINITIONS - CADASTRO DE PRODUTOS ====================

    @Given("que não existe um produto chamado {string}")
    public void que_não_existe_um_produto_chamado(String nomeProduto) {
        // Verifica que o produto realmente não existe no repositório
        assertFalse("Produto não deveria existir", repositorio.produtoExiste(nomeProduto));
        // Armazena o nome do produto para uso em outros steps
        produtoAtual = nomeProduto;
    }

    @When("eu cadastro um novo produto com o nome {string} e estoque inicial {int}")
    public void eu_cadastro_um_novo_produto_com_o_nome_e_estoque_inicial(String nomeProduto, Integer estoqueInicial) {
        try {
            // Tenta cadastrar o produto com preço padrão de R$ 10,00
            operacaoSucesso = repositorio.cadastrarProduto(nomeProduto, estoqueInicial, BigDecimal.valueOf(10.0));
            if (operacaoSucesso) {
                mensagemRetorno = "Produto cadastrado com sucesso";
            }
        } catch (Exception e) {
            // Captura qualquer exceção durante o cadastro
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @Then("o produto é cadastrado com sucesso")
    public void o_produto_é_cadastrado_com_sucesso() {
        // Verifica que a operação foi bem-sucedida
        assertTrue("Produto deveria ter sido cadastrado", operacaoSucesso);
        // Confirma que o produto realmente existe no repositório
        assertTrue("Produto deveria existir no repositório", repositorio.produtoExiste(produtoAtual));
    }

    @Given("que já existe um produto chamado {string}")
    public void que_já_existe_um_produto_chamado(String nomeProduto) {
        // Cadastra o produto previamente para simular que já existe
        repositorio.cadastrarProduto(nomeProduto, 10, BigDecimal.valueOf(15.0));
        // Confirma que o produto foi cadastrado corretamente
        assertTrue("Produto deveria existir", repositorio.produtoExiste(nomeProduto));
        // Armazena o nome para uso em outros steps
        produtoAtual = nomeProduto;
    }

    @When("eu tento cadastrar um novo produto com o nome {string}")
    public void eu_tento_cadastrar_um_novo_produto_com_o_nome(String nomeProduto) {
        try {
            // Tenta cadastrar produto com nome duplicado
            operacaoSucesso = repositorio.cadastrarProduto(nomeProduto, 10, BigDecimal.valueOf(10.0));
            if (!operacaoSucesso) {
                mensagemRetorno = "Produto já existe";
            }
        } catch (Exception e) {
            // Captura exceção esperada para nome duplicado
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @Then("o sistema rejeita a operação de cadastro com nome duplicado")
    public void o_sistema_rejeita_a_operação_de_cadastro_com_nome_duplicado() {
        assertFalse("A operação de cadastro deveria ter falhado devido ao nome duplicado", operacaoSucesso);
    }

    // ==================== STEP DEFINITIONS - ATUALIZAÇÃO DE ESTOQUE ====================
    
    @Given("que existe um produto {string} com estoque {int}")
    public void que_existe_um_produto_com_estoque(String nomeProduto, Integer estoque) {
        // Cadastra o produto com estoque inicial e preço padrão de R$ 20,00
        repositorio.cadastrarProduto(nomeProduto, estoque, BigDecimal.valueOf(20.0));
        produtoAtual = nomeProduto;
        
        // Valida que o produto foi criado corretamente
        EstoqueMockRepositorio.Produto produto = repositorio.obterProduto(nomeProduto);
        assertNotNull("Produto deveria existir", produto);
        assertEquals("Estoque deveria ser igual", estoque.intValue(), produto.getEstoque());
    }

    @When("eu adiciono {int} unidades ao estoque")
    public void eu_adiciono_unidades_ao_estoque(Integer quantidade) {
        try {
            // Atualiza o estoque somando a quantidade especificada
            operacaoSucesso = repositorio.atualizarEstoque(produtoAtual, quantidade);
        } catch (Exception e) {
            // Captura exceções durante a atualização
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @Then("o estoque atual do produto {string} passa a ser {int}")
    public void o_estoque_atual_do_produto_passa_a_ser(String nomeProduto, Integer estoqueEsperado) {
        // Obtém o produto do repositório
        EstoqueMockRepositorio.Produto produto = repositorio.obterProduto(nomeProduto);
        
        // Valida que o produto existe
        assertNotNull("Produto deveria existir no repositório", produto);
        
        // Valida que o estoque foi atualizado corretamente
        assertEquals("O estoque do produto deveria ser " + estoqueEsperado, 
                     estoqueEsperado.intValue(), 
                     produto.getEstoque());
        
        // Confirma que a operação foi bem-sucedida
        assertTrue("A operação de atualização deveria ter sido bem-sucedida", operacaoSucesso);
    }

    // ==================== STEP DEFINITIONS - VENDAS PDV ====================
    
    @When("eu envio a venda de {int} produtos {string} para registro")
    public void eu_envio_a_venda_de_produtos_para_registro(Integer quantidade, String nomeProduto) {
        try {
            // Processa a venda reduzindo o estoque automaticamente
            operacaoSucesso = repositorio.reduzirEstoque(nomeProduto, quantidade);
            if (operacaoSucesso) {
                mensagemRetorno = "Venda registrada com sucesso";
            } else {
                // Caso de falha por estoque insuficiente (retorno false)
                mensagemRetorno = "Estoque insuficiente";
            }
        } catch (Exception e) {
            // Captura exceções durante o processamento da venda (ex.: estoque insuficiente)
            excecaoLancada = e;
            operacaoSucesso = false;
            mensagemRetorno = "Estoque insuficiente";
        }
    }

    @Then("o sistema responde sucesso e registra a venda")
    public void o_sistema_responde_sucesso_e_registra_a_venda() {
        assertTrue("A operação de venda deveria ter sido bem-sucedida", operacaoSucesso);
    }

    @Then("o sistema rejeita a operação de venda com estoque insuficiente")
    public void o_sistema_rejeita_a_operação_de_venda_com_estoque_insuficiente() {
        assertFalse("A operação de venda deveria ter falhado devido ao estoque insuficiente", operacaoSucesso);
    }
}