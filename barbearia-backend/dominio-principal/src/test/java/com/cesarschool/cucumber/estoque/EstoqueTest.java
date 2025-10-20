package com.cesarschool.cucumber.estoque;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Classe de testes BDD (Behavior Driven Development) para funcionalidades de gestão de estoque e PDV.
 * Esta classe implementa os step definitions do Cucumber para validar os cenários de:
 * - Cadastro de produtos
 * - Controle de estoque (entrada/saída)
 * - Vendas no PDV (Ponto de Venda)
 * - Gestão de status de produtos (ativo/inativo)
 * - Alertas de estoque mínimo
 * - Relatórios de estoque
 * - Histórico de movimentações
 * 
 * @author Sistema de Barbearia
 * @version 1.0
 */
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
    
    /**
     * Método executado antes de cada cenário de teste.
     * Inicializa o repositório mock e reseta todas as variáveis de estado
     * para garantir que cada teste execute de forma independente.
     */
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
    
    /**
     * Verifica que um produto específico não existe no sistema.
     * Este step é usado como pré-condição para testar o cadastro de novos produtos.
     * 
     * @param nomeProduto Nome do produto que não deve existir
     * @cucumber.step "que não existe um produto chamado {string}"
     */
    @Given("que não existe um produto chamado {string}")
    public void que_não_existe_um_produto_chamado(String nomeProduto) {
        // Verifica que o produto realmente não existe no repositório
        assertFalse("Produto não deveria existir", repositorio.produtoExiste(nomeProduto));
        // Armazena o nome do produto para uso em outros steps
        produtoAtual = nomeProduto;
    }

    /**
     * Executa o cadastro de um novo produto com nome e estoque inicial especificados.
     * Testa o cenário positivo onde um produto válido é cadastrado no sistema.
     * 
     * @param nomeProduto Nome do produto a ser cadastrado
     * @param estoqueInicial Quantidade inicial de estoque
     * @cucumber.step "eu cadastro um novo produto com o nome {string} e estoque inicial {int}"
     */
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

    /**
     * Valida que o produto foi cadastrado com sucesso no sistema.
     * Verifica tanto o flag de sucesso quanto a existência do produto no repositório.
     * 
     * @cucumber.step "o produto é cadastrado com sucesso"
     */
    @Then("o produto é cadastrado com sucesso")
    public void o_produto_é_cadastrado_com_sucesso() {
        // Verifica que a operação foi bem-sucedida
        assertTrue("Produto deveria ter sido cadastrado", operacaoSucesso);
        // Confirma que o produto realmente existe no repositório
        assertTrue("Produto deveria existir no repositório", repositorio.produtoExiste(produtoAtual));
    }

    /**
     * Configura um produto que já existe no sistema.
     * Este step é usado como pré-condição para testar cenários de nome duplicado.
     * 
     * @param nomeProduto Nome do produto que já deve existir
     * @cucumber.step "que já existe um produto chamado {string}"
     */
    @Given("que já existe um produto chamado {string}")
    public void que_já_existe_um_produto_chamado(String nomeProduto) {
        // Cadastra o produto previamente para simular que já existe
        repositorio.cadastrarProduto(nomeProduto, 10, BigDecimal.valueOf(15.0));
        // Confirma que o produto foi cadastrado corretamente
        assertTrue("Produto deveria existir", repositorio.produtoExiste(nomeProduto));
        // Armazena o nome para uso em outros steps
        produtoAtual = nomeProduto;
    }

    /**
     * Tenta cadastrar um produto que já existe no sistema.
     * Testa o cenário negativo onde deve ocorrer falha por nome duplicado.
     * 
     * @param nomeProduto Nome do produto duplicado
     * @cucumber.step "eu tento cadastrar um novo produto com o nome {string}"
     */
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

    /**
     * Valida que o sistema rejeitou cadastro de produto com nome duplicado.
     * Verifica se a regra de negócio de unicidade de nomes está funcionando.
     * 
     * @cucumber.step "o sistema rejeita a operação de cadastro com nome duplicado"
     */
    @Then("o sistema rejeita a operação de cadastro com nome duplicado")
    public void o_sistema_rejeita_a_operação_de_cadastro_com_nome_duplicado() {
        assertFalse("A operação de cadastro deveria ter falhado devido ao nome duplicado", operacaoSucesso);
    }

    // ==================== STEP DEFINITIONS - ATUALIZAÇÃO DE ESTOQUE ====================
    
    /**
     * Configura um produto pré-existente com estoque definido.
     * Usado como pré-condição para testar operações de atualização de estoque.
     * 
     * @param nomeProduto Nome do produto a ser criado
     * @param estoque Quantidade inicial de estoque
     * @cucumber.step "que existe um produto {string} com estoque {int}"
     */
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

    /**
     * Executa a adição de unidades ao estoque do produto atual.
     * Testa o cenário de incremento positivo no estoque.
     * 
     * @param quantidade Número de unidades a adicionar
     * @cucumber.step "eu adiciono {int} unidades ao estoque"
     */
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

    // ==================== STEP DEFINITIONS - VENDAS PDV ====================
    
    /**
     * Registra uma venda no PDV (Ponto de Venda) reduzindo o estoque.
     * Simula o processo completo de venda com redução automática do estoque.
     * 
     * @param quantidade Número de produtos vendidos
     * @param nomeProduto Nome do produto vendido
     * @cucumber.step "eu envio a venda de {int} produtos {string} para registro"
     */
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

    /**
     * Valida que o sistema processou uma venda com sucesso.
     * Confirma que operações válidas são executadas corretamente.
     * 
     * @cucumber.step "o sistema responde sucesso e registra a venda"
     */
    @Then("o sistema responde sucesso e registra a venda")
    public void o_sistema_responde_sucesso_e_registra_a_venda() {
        assertTrue("A operação de venda deveria ter sido bem-sucedida", operacaoSucesso);
    }

    

        /**
     * Valida que o sistema rejeitou venda por estoque insuficiente.
     * Verifica se o controle de estoque está impedindo vendas inválidas.
     * 
     * @cucumber.step "o sistema rejeita a operação de venda com estoque insuficiente"
     */
    @Then("o sistema rejeita a operação de venda com estoque insuficiente")
    public void o_sistema_rejeita_a_operação_de_venda_com_estoque_insuficiente() {
        assertFalse("A operação de venda deveria ter falhado devido ao estoque insuficiente", operacaoSucesso);
    }
}