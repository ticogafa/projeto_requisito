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

    // ==================== STEP DEFINITIONS - AUTENTICAÇÃO ====================
    
    /**
     * Step definition para simular autenticação bem-sucedida do operador de PDV.
     * Este step é usado como pré-condição em vários cenários de teste.
     * 
     * @cucumber.step "que estou autenticado como operador de PDV"
     */
    @Given("que estou autenticado como operador de PDV")
    public void que_estou_autenticado_como_operador_de_pdv() {
        // Simula autenticação bem-sucedida definindo flag de sucesso
        operacaoSucesso = true;
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

    /**
     * Verifica se o estoque foi atualizado para o valor correto.
     * Valida que a operação de adição foi executada corretamente.
     * 
     * @param nomeProduto Nome do produto para verificar
     * @param estoqueEsperado Quantidade final esperada
     * @cucumber.step "o estoque atual do produto {string} passa a ser {int}"
     */
    @Then("o estoque atual do produto {string} passa a ser {int}")
    public void o_estoque_atual_do_produto_passa_a_ser(String nomeProduto, Integer estoqueEsperado) {
        // Obtém o produto atualizado
        EstoqueMockRepositorio.Produto produto = repositorio.obterProduto(nomeProduto);
        assertNotNull("Produto deveria existir", produto);
        // Compara o estoque atual com o esperado
        assertEquals("Estoque deveria ser atualizado", estoqueEsperado.intValue(), produto.getEstoque());
    }

    /**
     * Tenta reduzir o estoque diretamente com valor negativo.
     * Testa o cenário negativo de operação inválida de estoque.
     * 
     * @param quantidade Quantidade negativa para teste de validação
     * @cucumber.step "eu tento reduzir o estoque em {int} unidades diretamente"
     */
    @When("eu tento reduzir o estoque em {int} unidades diretamente")
    public void eu_tento_reduzir_o_estoque_em_unidades_diretamente(Integer quantidade) {
        try {
            // Tenta reduzir com valor negativo (operação inválida)
            operacaoSucesso = repositorio.atualizarEstoque(produtoAtual, quantidade);
            if (!operacaoSucesso) {
                mensagemRetorno = "Operação inválida";
            }
        } catch (Exception e) {
            // Captura exceção esperada para operação inválida
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
            }
        } catch (Exception e) {
            // Captura exceções durante o processamento da venda
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    /**
     * Tenta processar uma venda PDV que pode resultar em estoque insuficiente.
     * Testa o cenário onde a quantidade vendida excede o estoque disponível.
     * 
     * @param quantidade Número de produtos a vender
     * @param nomeProduto Nome do produto para venda
     * @cucumber.step "envio uma venda PDV com {int} unidades do produto {string}"
     */
    @When("envio uma venda PDV com {int} unidades do produto {string}")
    public void envio_uma_venda_pdv_com_unidades_do_produto(Integer quantidade, String nomeProduto) {
        try {
            // Tenta processar venda que pode exceder o estoque
            operacaoSucesso = repositorio.reduzirEstoque(nomeProduto, quantidade);
            if (!operacaoSucesso) {
                mensagemRetorno = "Estoque insuficiente";
            }
        } catch (Exception e) {
            // Captura exceção para estoque insuficiente
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    // ==================== STEP DEFINITIONS - STATUS DE PRODUTO ====================
    
    /**
     * Configura um produto ativo no sistema.
     * Usado para testar operações de ativação/desativação de produtos.
     * 
     * @param nomeProduto Nome do produto a ser criado como ativo
     * @cucumber.step "que existe um produto {string} ativo"
     */
    @Given("que existe um produto {string} ativo")
    public void que_existe_um_produto_ativo(String nomeProduto) {
        // Cadastra produto com estoque alto e preço R$ 25,00
        repositorio.cadastrarProduto(nomeProduto, 50, BigDecimal.valueOf(25.0));
        // Verifica que o produto está ativo por padrão
        assertTrue("Produto deveria estar ativo", repositorio.produtoAtivo(nomeProduto));
        produtoAtual = nomeProduto;
    }

    /**
     * Executa a desativação de um produto com motivo específico.
     * Testa o processo de inativação de produtos no sistema.
     * 
     * @param motivo Razão para desativação do produto
     * @cucumber.step "eu desativo o produto por motivo de {string}"
     */
    @When("eu desativo o produto por motivo de {string}")
    public void eu_desativo_o_produto_por_motivo_de(String motivo) {
        try {
            // Desativa o produto com o motivo especificado
            operacaoSucesso = repositorio.desativarProduto(produtoAtual, motivo);
        } catch (Exception e) {
            // Captura exceções durante a desativação
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    /**
     * Verifica se o produto aparece com o status correto no PDV.
     * Valida que o status de ativo/inativo é refletido corretamente no sistema.
     * 
     * @param status Status esperado ("Ativo" ou "Inativo")
     * @cucumber.step "o produto aparece como {string} na lista de vendas do PDV"
     */
    @Then("o produto aparece como {string} na lista de vendas do PDV")
    public void o_produto_aparece_como_na_lista_de_vendas_do_pdv(String status) {
        if ("Inativo".equals(status)) {
            assertFalse("Produto deveria estar inativo", repositorio.produtoAtivo(produtoAtual));
        } else {
            assertTrue("Produto deveria estar ativo", repositorio.produtoAtivo(produtoAtual));
        }
    }

    /**
     * Configura um produto que já está inativo por motivo específico.
     * Usado para testar tentativas de venda de produtos inativos.
     * 
     * @param nomeProduto Nome do produto inativo
     * @param motivo Razão da inativação
     * @cucumber.step "que o produto {string} está inativo por {string}"
     */
    @Given("que o produto {string} está inativo por {string}")
    public void que_o_produto_está_inativo_por(String nomeProduto, String motivo) {
        // Cadastra o produto normalmente
        repositorio.cadastrarProduto(nomeProduto, 10, BigDecimal.valueOf(30.0));
        // Em seguida, o desativa com o motivo especificado
        repositorio.desativarProduto(nomeProduto, motivo);
        // Confirma que está realmente inativo
        assertFalse("Produto deveria estar inativo", repositorio.produtoAtivo(nomeProduto));
        produtoAtual = nomeProduto;
    }

    /**
     * Tenta registrar uma venda de produto que pode estar inativo.
     * Testa a validação de status do produto antes de processar vendas.
     * 
     * @param nomeProduto Nome do produto para venda
     * @cucumber.step "o operador tenta registrar uma venda do produto {string}"
     */
    @When("o operador tenta registrar uma venda do produto {string}")
    public void o_operador_tenta_registrar_uma_venda_do_produto(String nomeProduto) {
        try {
            // Verifica se o produto está ativo antes de processar venda
            if (!repositorio.produtoAtivo(nomeProduto)) {
                operacaoSucesso = false;
                mensagemRetorno = "Produto inativo";
                return;
            }
            // Se ativo, processa a venda
            operacaoSucesso = repositorio.reduzirEstoque(nomeProduto, 1);
        } catch (Exception e) {
            // Captura exceções durante a tentativa de venda
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    // ==================== STEP DEFINITIONS - ALERTAS DE ESTOQUE ====================
    
    /**
     * Configura um produto com estoque mínimo definido para geração de alertas.
     * Estabelece o limite mínimo abaixo do qual o sistema deve gerar alertas.
     * 
     * @param nomeProduto Nome do produto para configuração
     * @param estoqueMinimo Quantidade mínima de estoque antes do alerta
     * @cucumber.step "que existe um produto {string} com estoque mínimo configurado para {int} unidades"
     */
    @Given("que existe um produto {string} com estoque mínimo configurado para {int} unidades")
    public void que_existe_um_produto_com_estoque_mínimo_configurado_para_unidades(String nomeProduto, Integer estoqueMinimo) {
        // Cadastra produto com estoque acima do mínimo (mínimo + 5)
        repositorio.cadastrarProduto(nomeProduto, estoqueMinimo + 5, BigDecimal.valueOf(15.0));
        // Define o limite mínimo para alertas
        repositorio.definirEstoqueMinimo(nomeProduto, estoqueMinimo);
        produtoAtual = nomeProduto;
    }

    /**
     * Simula uma venda que reduz o estoque a um nível específico.
     * Calcula automaticamente a quantidade necessária para atingir o estoque desejado.
     * 
     * @param nomeProduto Nome do produto vendido
     * @param novoEstoque Nível de estoque resultante após a venda
     * @cucumber.step "o estoque do produto {string} chega a {int} unidades após uma venda"
     */
    @When("o estoque do produto {string} chega a {int} unidades após uma venda")
    public void o_estoque_do_produto_chega_a_unidades_após_uma_venda(String nomeProduto, Integer novoEstoque) {
        // Obtém o produto para calcular a quantidade a vender
        EstoqueMockRepositorio.Produto produto = repositorio.obterProduto(nomeProduto);
        // Calcula quantas unidades devem ser vendidas para chegar ao estoque desejado
        int quantidadeVenda = produto.getEstoque() - novoEstoque;
        // Executa a venda para reduzir o estoque
        repositorio.reduzirEstoque(nomeProduto, quantidadeVenda);
    }

    /**
     * Verifica se o sistema gerou o alerta esperado para estoque baixo.
     * Valida que o monitoramento de estoque mínimo está funcionando corretamente.
     * 
     * @param tipoAlerta Tipo de alerta esperado pelo sistema
     * @cucumber.step "o sistema gera um alerta de {string}"
     */
    @Then("o sistema gera um alerta de {string}")
    public void o_sistema_gera_um_alerta_de(String tipoAlerta) {
        // Obtém a lista de alertas gerados pelo sistema
        List<String> alertas = repositorio.obterAlertas();
        // Verifica que há pelo menos um alerta
        assertTrue("Deveria haver alertas", !alertas.isEmpty());
        // Confirma que o alerta específico foi gerado
        assertTrue("Deveria conter o alerta esperado", alertas.contains(tipoAlerta));
    }

    /**
     * Configura um produto básico para testes gerais.
     * Step genérico usado quando apenas a existência do produto é necessária.
     * 
     * @param nomeProduto Nome do produto a ser criado
     * @cucumber.step "que existe um produto {string}"
     */
    @Given("que existe um produto {string}")
    public void que_existe_um_produto(String nomeProduto) {
        // Cadastra produto com valores padrão para testes gerais
        repositorio.cadastrarProduto(nomeProduto, 20, BigDecimal.valueOf(12.0));
        produtoAtual = nomeProduto;
    }

    /**
     * Tenta configurar estoque mínimo com valor inválido (negativo).
     * Testa a validação de entrada para configuração de estoque mínimo.
     * 
     * @cucumber.step "eu tento configurar o estoque mínimo para um valor negativo"
     */
    @When("eu tento configurar o estoque mínimo para um valor negativo")
    public void eu_tento_configurar_o_estoque_mínimo_para_um_valor_negativo() {
        try {
            // Tenta configurar estoque mínimo com valor inválido (-5)
            operacaoSucesso = repositorio.definirEstoqueMinimo(produtoAtual, -5);
            if (!operacaoSucesso) {
                mensagemRetorno = "Valor inválido";
            }
        } catch (Exception e) {
            // Captura exceção esperada para valor inválido
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    // ==================== STEP DEFINITIONS - RELATÓRIOS ====================
    
    /**
     * Configura cenário com múltiplos produtos em estoque baixo.
     * Prepara dados para testar a geração de relatórios de estoque insuficiente.
     * 
     * @cucumber.step "que existem produtos com estoque abaixo do mínimo configurado"
     */
    @Given("que existem produtos com estoque abaixo do mínimo configurado")
    public void que_existem_produtos_com_estoque_abaixo_do_mínimo_configurado() {
        // Produto A: estoque 5, mínimo 10 (abaixo do mínimo)
        repositorio.cadastrarProduto("Produto A", 5, BigDecimal.valueOf(10.0));
        repositorio.definirEstoqueMinimo("Produto A", 10);
        
        // Produto B: estoque 2, mínimo 5 (abaixo do mínimo)
        repositorio.cadastrarProduto("Produto B", 2, BigDecimal.valueOf(15.0));
        repositorio.definirEstoqueMinimo("Produto B", 5);
    }

    /**
     * Solicita a geração do relatório de produtos com estoque baixo.
     * Executa a consulta que identifica produtos abaixo do estoque mínimo.
     * 
     * @cucumber.step "eu solicito o relatório de estoque baixo"
     */
    @When("eu solicito o relatório de estoque baixo")
    public void eu_solicito_o_relatório_de_estoque_baixo() {
        try {
            // Obtém lista de produtos com estoque abaixo do mínimo
            produtosBaixo = repositorio.obterProdutosComEstoqueBaixo();
            operacaoSucesso = true;
        } catch (Exception e) {
            // Captura exceções durante a geração do relatório
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    /**
     * Verifica se o relatório contém os produtos com estoque insuficiente.
     * Valida que o sistema identifica corretamente produtos abaixo do mínimo.
     * 
     * @cucumber.step "o sistema exibe a lista de produtos com estoque insuficiente"
     */
    @Then("o sistema exibe a lista de produtos com estoque insuficiente")
    public void o_sistema_exibe_a_lista_de_produtos_com_estoque_insuficiente() {
        // Verifica que o relatório foi gerado
        assertNotNull("Lista não deveria ser nula", produtosBaixo);
        // Confirma que há produtos identificados com estoque baixo
        assertTrue("Deveria haver produtos com estoque baixo", !produtosBaixo.isEmpty());
    }

    /**
     * Configura um produto com histórico de movimentações variadas.
     * Simula operações de entrada e saída para testar relatórios de histórico.
     * 
     * @param nomeProduto Nome do produto para criar histórico
     * @cucumber.step "que existe um produto {string} com histórico de movimentações"
     */
    @Given("que existe um produto {string} com histórico de movimentações")
    public void que_existe_um_produto_com_histórico_de_movimentações(String nomeProduto) {
        // Cadastra produto com estoque inicial alto
        repositorio.cadastrarProduto(nomeProduto, 100, BigDecimal.valueOf(20.0));
        // Simula entrada de estoque
        repositorio.atualizarEstoque(nomeProduto, 50);
        // Simula venda (saída de estoque)
        repositorio.reduzirEstoque(nomeProduto, 10);
        produtoAtual = nomeProduto;
    }

    /**
     * Consulta o histórico completo de movimentações do produto.
     * Executa a busca por todas as operações realizadas com o produto.
     * 
     * @cucumber.step "eu consulto o histórico de movimentação do produto"
     */
    @When("eu consulto o histórico de movimentação do produto")
    public void eu_consulto_o_histórico_de_movimentação_do_produto() {
        try {
            // Obtém histórico completo de movimentações
            historicoConsultado = repositorio.obterHistorico(produtoAtual);
            operacaoSucesso = true;
        } catch (Exception e) {
            // Captura exceções durante a consulta
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    /**
     * Verifica se o histórico contém todas as movimentações esperadas.
     * Valida que entradas, saídas e vendas estão registradas corretamente.
     * 
     * @cucumber.step "o sistema exibe todas as entradas, saídas e vendas registradas"
     */
    @Then("o sistema exibe todas as entradas, saídas e vendas registradas")
    public void o_sistema_exibe_todas_as_entradas_saídas_e_vendas_registradas() {
        // Verifica que o histórico foi obtido
        assertNotNull("Histórico não deveria ser nulo", historicoConsultado);
        assertTrue("Deveria haver movimentações", !historicoConsultado.isEmpty());
        
        // Verifica presença de operações de entrada
        boolean temEntrada = historicoConsultado.stream()
            .anyMatch(m -> "ENTRADA".equals(m.getTipo()));
        // Verifica presença de operações de venda
        boolean temVenda = historicoConsultado.stream()
            .anyMatch(m -> "VENDA".equals(m.getTipo()));
        
        // Confirma que ambos os tipos de movimentação estão presentes
        assertTrue("Deveria ter pelo menos uma entrada", temEntrada);
        assertTrue("Deveria ter pelo menos uma venda", temVenda);
    }

    // ==================== STEP DEFINITIONS - VALIDAÇÕES ESPECÍFICAS ====================
    
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

    /**
     * Valida que o sistema rejeitou atualização com valor negativo de estoque.
     * Verifica se a validação de entrada para valores inválidos está ativa.
     * 
     * @cucumber.step "o sistema rejeita a operação de atualização com valor negativo"
     */
    @Then("o sistema rejeita a operação de atualização com valor negativo")
    public void o_sistema_rejeita_a_operação_de_atualização_com_valor_negativo() {
        assertFalse("A operação de atualização deveria ter falhado devido ao valor negativo", operacaoSucesso);
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

    /**
     * Valida que o sistema rejeitou venda de produto inativo.
     * Verifica se o controle de status está impedindo vendas de produtos desativados.
     * 
     * @cucumber.step "o sistema rejeita a operação de venda de produto inativo"
     */
    @Then("o sistema rejeita a operação de venda de produto inativo")
    public void o_sistema_rejeita_a_operação_de_venda_de_produto_inativo() {
        assertFalse("A operação de venda deveria ter falhado devido ao produto inativo", operacaoSucesso);
    }

    /**
     * Valida que o sistema rejeitou configuração de estoque mínimo inválido.
     * Verifica se a validação impede valores negativos para estoque mínimo.
     * 
     * @cucumber.step "o sistema rejeita a operação de definição de estoque mínimo inválido"
     */
    @Then("o sistema rejeita a operação de definição de estoque mínimo inválido")
    public void o_sistema_rejeita_a_operação_de_definição_de_estoque_mínimo_inválido() {
        assertFalse("A operação de definição de estoque mínimo deveria ter falhado devido ao valor inválido", operacaoSucesso);
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
}
