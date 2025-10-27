package com.cesarschool.cucumber.estoque;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import com.cesarschool.barbearia.dominio.principal.produto.Produto;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoId;
import com.cesarschool.barbearia.dominio.principal.produto.estoque.GestaoEstoqueServico;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Testes BDD para o sistema de gestão de estoque.
 * 
 * Este teste utiliza os serviços REAIS do domínio principal:
 * - GestaoEstoqueServico: Serviço de domínio com todas as regras de negócio
 * - ProdutoMockRepositorio: Repositório mock para simular persistência
 * - MovimentacaoEstoqueMockRepositorio: Repositório mock para histórico
 * 
 * Testa os cenários definidos em Estoque.feature:
 * - Cadastro de produtos com nome único
 * - Atualização de estoque
 * - Registro de vendas no PDV
 * - Validação de estoque insuficiente
 */
public class EstoqueTest {
    
    // ==================== ATRIBUTOS DE CONTROLE DE TESTE ====================
    
    /** Repositório mock para produtos */
    private ProdutoMockRepositorio produtoRepositorio;
    
    /** Repositório mock para movimentações */
    private MovimentacaoEstoqueMockRepositorio movimentacaoRepositorio;
    
    /** Serviço real de gestão de estoque (código do domínio principal) */
    private GestaoEstoqueServico gestaoEstoqueServico;
    
    /** Flag para indicar se a última operação foi executada com sucesso */
    private boolean operacaoSucesso;
    
    /** Mensagem de retorno da última operação executada */
    private String mensagemRetorno;
    
    /** Exceção capturada durante a execução de operações que falharam */
    private Exception excecaoLancada;
    
    /** Produto sendo manipulado no teste atual */
    private Produto produtoAtual;
    
    /** Nome do usuário executando as operações de teste */
    private static final String USUARIO_TESTE = "teste-cucumber";
    
    // ==================== CONFIGURAÇÃO DE TESTE ====================
    
    @Before
    public void setUp() {
        // Inicializa os repositórios mock
        produtoRepositorio = new ProdutoMockRepositorio();
        movimentacaoRepositorio = new MovimentacaoEstoqueMockRepositorio();
        
        // Inicializa o serviço REAL de gestão de estoque
        gestaoEstoqueServico = new GestaoEstoqueServico(
            produtoRepositorio,
            movimentacaoRepositorio
        );
        
        // Reset das variáveis de controle de estado
        operacaoSucesso = false;
        mensagemRetorno = "";
        excecaoLancada = null;
        produtoAtual = null;
        
        // Limpa todos os dados dos repositórios para começar com estado limpo
        produtoRepositorio.limparDados();
        movimentacaoRepositorio.limparDados();
    }

    // ==================== STEP DEFINITIONS - CADASTRO DE PRODUTOS ====================

    @Given("que não existe um produto chamado {string}")
    public void que_não_existe_um_produto_chamado(String nomeProduto) {
        // Verifica que o produto realmente não existe no repositório
        Produto produto = produtoRepositorio.buscarPorNome(nomeProduto);
        assertNull("Produto não deveria existir", produto);
    }

    @When("eu cadastro um novo produto com o nome {string} e estoque inicial {int}")
    public void eu_cadastro_um_novo_produto_com_o_nome_e_estoque_inicial(String nomeProduto, Integer estoqueInicial) {
        try {
            // Cria o produto com ID temporário (será substituído pelo repositório)
            Produto novoProduto = new Produto(
                -1, // ID temporário - será substituído pelo repositório
                nomeProduto,
                estoqueInicial,
                BigDecimal.valueOf(10.0),
                5 // estoque mínimo padrão
            );
            
            // Usa o serviço REAL para cadastrar
            produtoAtual = gestaoEstoqueServico.cadastrarProduto(novoProduto, USUARIO_TESTE);
            operacaoSucesso = true;
            mensagemRetorno = "Produto cadastrado com sucesso";
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
        assertNotNull("Produto atual deveria estar definido", produtoAtual);
        
        // Confirma que o produto realmente existe no repositório
        Produto produto = produtoRepositorio.buscarPorNome(produtoAtual.getNome());
        assertNotNull("Produto deveria existir no repositório", produto);
    }

    @Given("que já existe um produto chamado {string}")
    public void que_já_existe_um_produto_chamado(String nomeProduto) {
        // Cria e cadastra o produto previamente com ID temporário
        Produto produto = new Produto(
            -1, // ID temporário
            nomeProduto,
            10,
            BigDecimal.valueOf(15.0),
            5
        );
        
        produtoAtual = gestaoEstoqueServico.cadastrarProduto(produto, USUARIO_TESTE);
        
        // Confirma que o produto foi cadastrado corretamente
        assertNotNull("Produto deveria ter sido criado", produtoAtual);
        assertTrue("Produto deveria ter ID gerado", produtoAtual.getId() > 0);
    }

    @When("eu tento cadastrar um novo produto com o nome {string}")
    public void eu_tento_cadastrar_um_novo_produto_com_o_nome(String nomeProduto) {
        try {
            // Tenta cadastrar produto com nome duplicado com ID temporário
            Produto novoProduto = new Produto(
                -1, // ID temporário
                nomeProduto,
                10,
                BigDecimal.valueOf(10.0),
                5
            );
            
            gestaoEstoqueServico.cadastrarProduto(novoProduto, USUARIO_TESTE);
            operacaoSucesso = true;
        } catch (IllegalArgumentException e) {
            // Captura exceção esperada para nome duplicado
            excecaoLancada = e;
            operacaoSucesso = false;
            mensagemRetorno = "Produto já existe";
        } catch (Exception e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @Then("o sistema rejeita a operação de cadastro com nome duplicado")
    public void o_sistema_rejeita_a_operação_de_cadastro_com_nome_duplicado() {
        assertFalse("A operação de cadastro deveria ter falhado devido ao nome duplicado", operacaoSucesso);
        assertNotNull("Uma exceção deveria ter sido lançada", excecaoLancada);
        assertTrue("A exceção deveria mencionar nome duplicado", 
            excecaoLancada.getMessage().toLowerCase().contains("já existe") ||
            excecaoLancada.getMessage().toLowerCase().contains("cadastrado"));
    }

    // ==================== STEP DEFINITIONS - ATUALIZAÇÃO DE ESTOQUE ====================
    
    @Given("que existe um produto {string} com estoque {int}")
    public void que_existe_um_produto_com_estoque(String nomeProduto, Integer estoque) {
        // Cria e cadastra o produto com estoque inicial e ID temporário
        Produto produto = new Produto(
            -1, // ID temporário
            nomeProduto,
            estoque,
            BigDecimal.valueOf(20.0),
            5 // estoque mínimo
        );
        
        produtoAtual = gestaoEstoqueServico.cadastrarProduto(produto, USUARIO_TESTE);
        
        // Valida que o produto foi criado corretamente
        assertNotNull("Produto deveria ter sido criado", produtoAtual);
        assertEquals("Estoque deveria ser igual", estoque.intValue(), produtoAtual.getEstoque());
    }

    @When("eu adiciono {int} unidades ao estoque")
    public void eu_adiciono_unidades_ao_estoque(Integer quantidade) {
        try {
            // Usa o serviço REAL para adicionar estoque
            ProdutoId produtoId = new ProdutoId(produtoAtual.getId());
            produtoAtual = gestaoEstoqueServico.adicionarEstoque(
                produtoId,
                quantidade,
                "Entrada de estoque via teste",
                USUARIO_TESTE
            );
            operacaoSucesso = true;
        } catch (Exception e) {
            // Captura exceções durante a atualização
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @Then("o estoque atual do produto {string} passa a ser {int}")
    public void o_estoque_atual_do_produto_passa_a_ser(String nomeProduto, Integer estoqueEsperado) {
        // Obtém o produto do repositório
        Produto produto = produtoRepositorio.buscarPorNome(nomeProduto);
        
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
            // Busca o produto pelo nome para obter o ID
            Produto produto = produtoRepositorio.buscarPorNome(nomeProduto);
            assertNotNull("Produto deveria existir para venda", produto);
            
            ProdutoId produtoId = new ProdutoId(produto.getId());
            
            // Usa o serviço REAL para processar a venda
            produtoAtual = gestaoEstoqueServico.registrarVendaPDV(
                produtoId,
                quantidade,
                USUARIO_TESTE
            );
            
            operacaoSucesso = true;
            mensagemRetorno = "Venda registrada com sucesso";
        } catch (IllegalStateException e) {
            // Captura exceção de estoque insuficiente
            excecaoLancada = e;
            operacaoSucesso = false;
            mensagemRetorno = "Estoque insuficiente";
        } catch (Exception e) {
            // Captura outras exceções
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @Then("o sistema responde sucesso e registra a venda")
    public void o_sistema_responde_sucesso_e_registra_a_venda() {
        assertTrue("A operação de venda deveria ter sido bem-sucedida", operacaoSucesso);
        assertNotNull("Produto deveria ter sido atualizado", produtoAtual);
    }

    @Then("o sistema rejeita a operação de venda com estoque insuficiente")
    public void o_sistema_rejeita_a_operação_de_venda_com_estoque_insuficiente() {
        assertFalse("A operação de venda deveria ter falhado devido ao estoque insuficiente", operacaoSucesso);
        assertNotNull("Uma exceção deveria ter sido lançada", excecaoLancada);
        assertTrue("A exceção deveria mencionar estoque insuficiente",
            excecaoLancada.getMessage().toLowerCase().contains("estoque insuficiente"));
    }
}