package com.cesarschool.cucumber.estoque;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class EstoqueTest {
    
    private EstoqueMockRepositorio repositorio;
    private boolean operacaoSucesso;
    private String mensagemRetorno;
    private Exception excecaoLancada;
    private String produtoAtual;
    private List<EstoqueMockRepositorio.Produto> produtosBaixo;
    private List<EstoqueMockRepositorio.MovimentacaoEstoque> historicoConsultado;
    
    @Before
    public void setUp() {
        repositorio = new EstoqueMockRepositorio();
        operacaoSucesso = false;
        mensagemRetorno = "";
        excecaoLancada = null;
        produtoAtual = "";
        produtosBaixo = null;
        historicoConsultado = null;
        
        // Initialize system for stock tests
        repositorio.limparDados();
    }

    @Given("que estou autenticado como operador de PDV")
    public void que_estou_autenticado_como_operador_de_pdv() {
        // Simular autenticação bem-sucedida
        operacaoSucesso = true;
    }

    // Cenários de Cadastro de Produtos
    @Given("que não existe um produto chamado {string}")
    public void que_não_existe_um_produto_chamado(String nomeProduto) {
        assertFalse("Produto não deveria existir", repositorio.produtoExiste(nomeProduto));
        produtoAtual = nomeProduto;
    }

    @When("eu cadastro um novo produto com o nome {string} e estoque inicial {int}")
    public void eu_cadastro_um_novo_produto_com_o_nome_e_estoque_inicial(String nomeProduto, Integer estoqueInicial) {
        try {
            operacaoSucesso = repositorio.cadastrarProduto(nomeProduto, estoqueInicial, BigDecimal.valueOf(10.0));
            if (operacaoSucesso) {
                mensagemRetorno = "Produto cadastrado com sucesso";
            }
        } catch (Exception e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @Then("o produto é cadastrado com sucesso")
    public void o_produto_é_cadastrado_com_sucesso() {
        assertTrue("Produto deveria ter sido cadastrado", operacaoSucesso);
        assertTrue("Produto deveria existir no repositório", repositorio.produtoExiste(produtoAtual));
    }

    @Given("que já existe um produto chamado {string}")
    public void que_já_existe_um_produto_chamado(String nomeProduto) {
        repositorio.cadastrarProduto(nomeProduto, 10, BigDecimal.valueOf(15.0));
        assertTrue("Produto deveria existir", repositorio.produtoExiste(nomeProduto));
        produtoAtual = nomeProduto;
    }

    @When("eu tento cadastrar um novo produto com o nome {string}")
    public void eu_tento_cadastrar_um_novo_produto_com_o_nome(String nomeProduto) {
        try {
            operacaoSucesso = repositorio.cadastrarProduto(nomeProduto, 10, BigDecimal.valueOf(10.0));
            if (!operacaoSucesso) {
                mensagemRetorno = "Produto já existe";
            }
        } catch (Exception e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    // Cenários de Atualização de Estoque
    @Given("que existe um produto {string} com estoque {int}")
    public void que_existe_um_produto_com_estoque(String nomeProduto, Integer estoque) {
        repositorio.cadastrarProduto(nomeProduto, estoque, BigDecimal.valueOf(20.0));
        produtoAtual = nomeProduto;
        
        EstoqueMockRepositorio.Produto produto = repositorio.obterProduto(nomeProduto);
        assertNotNull("Produto deveria existir", produto);
        assertEquals("Estoque deveria ser igual", estoque.intValue(), produto.getEstoque());
    }

    @When("eu adiciono {int} unidades ao estoque")
    public void eu_adiciono_unidades_ao_estoque(Integer quantidade) {
        try {
            operacaoSucesso = repositorio.atualizarEstoque(produtoAtual, quantidade);
        } catch (Exception e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @Then("o estoque atual do produto {string} passa a ser {int}")
    public void o_estoque_atual_do_produto_passa_a_ser(String nomeProduto, Integer estoqueEsperado) {
        EstoqueMockRepositorio.Produto produto = repositorio.obterProduto(nomeProduto);
        assertNotNull("Produto deveria existir", produto);
        assertEquals("Estoque deveria ser atualizado", estoqueEsperado.intValue(), produto.getEstoque());
    }

    @When("eu tento reduzir o estoque em {int} unidades diretamente")
    public void eu_tento_reduzir_o_estoque_em_unidades_diretamente(Integer quantidade) {
        try {
            // Tentar reduzir com valor negativo (operação inválida)
            operacaoSucesso = repositorio.atualizarEstoque(produtoAtual, quantidade);
            if (!operacaoSucesso) {
                mensagemRetorno = "Operação inválida";
            }
        } catch (Exception e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    // Cenários de Venda PDV
    @When("eu envio a venda de {int} produtos {string} para registro")
    public void eu_envio_a_venda_de_produtos_para_registro(Integer quantidade, String nomeProduto) {
        try {
            operacaoSucesso = repositorio.reduzirEstoque(nomeProduto, quantidade);
            if (operacaoSucesso) {
                mensagemRetorno = "Venda registrada com sucesso";
            }
        } catch (Exception e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @When("envio uma venda PDV com {int} unidades do produto {string}")
    public void envio_uma_venda_pdv_com_unidades_do_produto(Integer quantidade, String nomeProduto) {
        try {
            operacaoSucesso = repositorio.reduzirEstoque(nomeProduto, quantidade);
            if (!operacaoSucesso) {
                mensagemRetorno = "Estoque insuficiente";
            }
        } catch (Exception e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    // Cenários de Status de Produto
    @Given("que existe um produto {string} ativo")
    public void que_existe_um_produto_ativo(String nomeProduto) {
        repositorio.cadastrarProduto(nomeProduto, 50, BigDecimal.valueOf(25.0));
        assertTrue("Produto deveria estar ativo", repositorio.produtoAtivo(nomeProduto));
        produtoAtual = nomeProduto;
    }

    @When("eu desativo o produto por motivo de {string}")
    public void eu_desativo_o_produto_por_motivo_de(String motivo) {
        try {
            operacaoSucesso = repositorio.desativarProduto(produtoAtual, motivo);
        } catch (Exception e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @Then("o produto aparece como {string} na lista de vendas do PDV")
    public void o_produto_aparece_como_na_lista_de_vendas_do_pdv(String status) {
        if ("Inativo".equals(status)) {
            assertFalse("Produto deveria estar inativo", repositorio.produtoAtivo(produtoAtual));
        } else {
            assertTrue("Produto deveria estar ativo", repositorio.produtoAtivo(produtoAtual));
        }
    }

    @Given("que o produto {string} está inativo por {string}")
    public void que_o_produto_está_inativo_por(String nomeProduto, String motivo) {
        repositorio.cadastrarProduto(nomeProduto, 10, BigDecimal.valueOf(30.0));
        repositorio.desativarProduto(nomeProduto, motivo);
        assertFalse("Produto deveria estar inativo", repositorio.produtoAtivo(nomeProduto));
        produtoAtual = nomeProduto;
    }

    @When("o operador tenta registrar uma venda do produto {string}")
    public void o_operador_tenta_registrar_uma_venda_do_produto(String nomeProduto) {
        try {
            if (!repositorio.produtoAtivo(nomeProduto)) {
                operacaoSucesso = false;
                mensagemRetorno = "Produto inativo";
                return;
            }
            operacaoSucesso = repositorio.reduzirEstoque(nomeProduto, 1);
        } catch (Exception e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    // Cenários de Alerta de Estoque
    @Given("que existe um produto {string} com estoque mínimo configurado para {int} unidades")
    public void que_existe_um_produto_com_estoque_mínimo_configurado_para_unidades(String nomeProduto, Integer estoqueMinimo) {
        repositorio.cadastrarProduto(nomeProduto, estoqueMinimo + 5, BigDecimal.valueOf(15.0));
        repositorio.definirEstoqueMinimo(nomeProduto, estoqueMinimo);
        produtoAtual = nomeProduto;
    }

    @When("o estoque do produto {string} chega a {int} unidades após uma venda")
    public void o_estoque_do_produto_chega_a_unidades_após_uma_venda(String nomeProduto, Integer novoEstoque) {
        EstoqueMockRepositorio.Produto produto = repositorio.obterProduto(nomeProduto);
        int quantidadeVenda = produto.getEstoque() - novoEstoque;
        repositorio.reduzirEstoque(nomeProduto, quantidadeVenda);
    }

    @Then("o sistema gera um alerta de {string}")
    public void o_sistema_gera_um_alerta_de(String tipoAlerta) {
        List<String> alertas = repositorio.obterAlertas();
        assertTrue("Deveria haver alertas", !alertas.isEmpty());
        assertTrue("Deveria conter o alerta esperado", alertas.contains(tipoAlerta));
    }

    @Given("que existe um produto {string}")
    public void que_existe_um_produto(String nomeProduto) {
        repositorio.cadastrarProduto(nomeProduto, 20, BigDecimal.valueOf(12.0));
        produtoAtual = nomeProduto;
    }

    @When("eu tento configurar o estoque mínimo para um valor negativo")
    public void eu_tento_configurar_o_estoque_mínimo_para_um_valor_negativo() {
        try {
            operacaoSucesso = repositorio.definirEstoqueMinimo(produtoAtual, -5);
            if (!operacaoSucesso) {
                mensagemRetorno = "Valor inválido";
            }
        } catch (Exception e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    // Cenários de Relatório
    @Given("que existem produtos com estoque abaixo do mínimo configurado")
    public void que_existem_produtos_com_estoque_abaixo_do_mínimo_configurado() {
        repositorio.cadastrarProduto("Produto A", 5, BigDecimal.valueOf(10.0));
        repositorio.definirEstoqueMinimo("Produto A", 10);
        
        repositorio.cadastrarProduto("Produto B", 2, BigDecimal.valueOf(15.0));
        repositorio.definirEstoqueMinimo("Produto B", 5);
    }

    @When("eu solicito o relatório de estoque baixo")
    public void eu_solicito_o_relatório_de_estoque_baixo() {
        try {
            produtosBaixo = repositorio.obterProdutosComEstoqueBaixo();
            operacaoSucesso = true;
        } catch (Exception e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @Then("o sistema exibe a lista de produtos com estoque insuficiente")
    public void o_sistema_exibe_a_lista_de_produtos_com_estoque_insuficiente() {
        assertNotNull("Lista não deveria ser nula", produtosBaixo);
        assertTrue("Deveria haver produtos com estoque baixo", !produtosBaixo.isEmpty());
    }

    @Given("que existe um produto {string} com histórico de movimentações")
    public void que_existe_um_produto_com_histórico_de_movimentações(String nomeProduto) {
        repositorio.cadastrarProduto(nomeProduto, 100, BigDecimal.valueOf(20.0));
        repositorio.atualizarEstoque(nomeProduto, 50); // Entrada
        repositorio.reduzirEstoque(nomeProduto, 10); // Venda
        produtoAtual = nomeProduto;
    }

    @When("eu consulto o histórico de movimentação do produto")
    public void eu_consulto_o_histórico_de_movimentação_do_produto() {
        try {
            historicoConsultado = repositorio.obterHistorico(produtoAtual);
            operacaoSucesso = true;
        } catch (Exception e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @Then("o sistema exibe todas as entradas, saídas e vendas registradas")
    public void o_sistema_exibe_todas_as_entradas_saídas_e_vendas_registradas() {
        assertNotNull("Histórico não deveria ser nulo", historicoConsultado);
        assertTrue("Deveria haver movimentações", !historicoConsultado.isEmpty());
        
        boolean temEntrada = historicoConsultado.stream()
            .anyMatch(m -> "ENTRADA".equals(m.getTipo()));
        boolean temVenda = historicoConsultado.stream()
            .anyMatch(m -> "VENDA".equals(m.getTipo()));
        
        assertTrue("Deveria ter pelo menos uma entrada", temEntrada);
        assertTrue("Deveria ter pelo menos uma venda", temVenda);
    }

    // Specific rejection step definitions
    @Then("o sistema rejeita a operação de cadastro com nome duplicado")
    public void o_sistema_rejeita_a_operação_de_cadastro_com_nome_duplicado() {
        assertFalse("A operação de cadastro deveria ter falhado devido ao nome duplicado", operacaoSucesso);
    }

    @Then("o sistema rejeita a operação de atualização com valor negativo")
    public void o_sistema_rejeita_a_operação_de_atualização_com_valor_negativo() {
        assertFalse("A operação de atualização deveria ter falhado devido ao valor negativo", operacaoSucesso);
    }

    @Then("o sistema rejeita a operação de venda com estoque insuficiente")
    public void o_sistema_rejeita_a_operação_de_venda_com_estoque_insuficiente() {
        assertFalse("A operação de venda deveria ter falhado devido ao estoque insuficiente", operacaoSucesso);
    }

    @Then("o sistema rejeita a operação de venda de produto inativo")
    public void o_sistema_rejeita_a_operação_de_venda_de_produto_inativo() {
        assertFalse("A operação de venda deveria ter falhado devido ao produto inativo", operacaoSucesso);
    }

    @Then("o sistema rejeita a operação de definição de estoque mínimo inválido")
    public void o_sistema_rejeita_a_operação_de_definição_de_estoque_mínimo_inválido() {
        assertFalse("A operação de definição de estoque mínimo deveria ter falhado devido ao valor inválido", operacaoSucesso);
    }

    @Then("o sistema responde sucesso e registra a venda")
    public void o_sistema_responde_sucesso_e_registra_a_venda() {
        assertTrue("A operação de venda deveria ter sido bem-sucedida", operacaoSucesso);
    }
}
