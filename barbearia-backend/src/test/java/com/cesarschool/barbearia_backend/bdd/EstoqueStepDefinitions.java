package com.cesarschool.barbearia_backend.bdd;

import com.cesarschool.barbearia_backend.vendas.controller.PdvController.VendaRequest;
import com.cesarschool.barbearia_backend.vendas.controller.PdvController.ItemRequest;
import com.cesarschool.barbearia_backend.vendas.model.Produto;
import com.cesarschool.barbearia_backend.vendas.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class EstoqueStepDefinitions extends CucumberSpringContext {

    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;

    private final List<ItemRequest> itensVenda = new ArrayList<>();
    private ResultActions resposta;
    // Campos auxiliares removidos (não necessários após simplificação dos cenários)

    // ------------- BACKGROUND -------------
    @Given("que o sistema está operacional")
    public void que_o_sistema_está_operacional() {
        // Apenas sanity check mínimo no repositório (lazy)
        long count = produtoRepository.count();
        assertThat(count).isGreaterThanOrEqualTo(0); // não lança
    }

    @Given("que estou autenticado como operador de PDV")
    public void que_estou_autenticado_como_operador_de_pdv() {
        // Segurança desabilitada (addFilters=false em @AutoConfigureMockMvc). Nada a fazer aqui.
    }

    // ------------- DADOS DE PRODUTO -------------
    @Given("que existe um produto {string} com estoque {int} e estoque mínimo {int}")
    public void que_existe_um_produto_com_estoque_e_estoque_mínimo(String nome, Integer estoque, Integer estoqueMinimo) {
        Optional<Produto> existente = produtoRepository.findAll().stream()
            .filter(p -> p.getNome().equalsIgnoreCase(nome))
            .findFirst();
        Produto p = existente.orElseGet(() -> {
            Produto novo = new Produto(nome, new BigDecimal("10.00")); // preço default, será sobrescrito via item
            novo.setEstoque(estoque);
            novo.setEstoqueMinimo(estoqueMinimo);
            return produtoRepository.save(novo);
        });
        // Se já existia, ajusta estoque para estado desejado (idempotência)
        if (p.getEstoque() != estoque || p.getEstoqueMinimo() != estoqueMinimo) {
            p.setEstoque(estoque);
            p.setEstoqueMinimo(estoqueMinimo);
            produtoRepository.save(p);
        }
    }

    // ------------- MONTAGEM DA VENDA -------------
    @Given("que monto uma venda PDV com {int} unidades do produto {string} preço unitário {double}")
    public void que_monto_uma_venda_pdv_com_unidades_do_produto_preco_unitario(Integer quantidade, String nomeProduto, Double precoUnitario) {
        Produto produto = produtoRepository.findAll().stream()
            .filter(p -> p.getNome().equalsIgnoreCase(nomeProduto))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Produto não encontrado para montar venda: " + nomeProduto));

        ItemRequest item = new ItemRequest();
        item.setTipo("PRODUTO");
        item.setProdutoId(produto.getId());
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(BigDecimal.valueOf(precoUnitario));
        item.setDescricao(nomeProduto);
        itensVenda.add(item);
    }

    // ------------- AÇÕES -------------
    @When("eu envio a venda para registro")
    public void eu_envio_a_venda_para_registro() throws Exception {
        VendaRequest req = new VendaRequest();
        req.setItens(itensVenda);

        resposta = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/pdv/vendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        );
    }

    // ------------- ASSERTS POSITIVOS -------------
    @Then("o sistema responde sucesso")
    public void o_sistema_responde_sucesso() throws Exception {
        resposta.andExpect(status().is2xxSuccessful());
    }

    @Then("o estoque atual do produto {string} passa a ser {int}")
    public void o_estoque_atual_do_produto_passa_a_ser(String nomeProduto, Integer esperado) throws Exception {
        // força flush consultando repositório após resposta
        Produto p = produtoRepository.findAll().stream()
            .filter(prod -> prod.getNome().equalsIgnoreCase(nomeProduto))
            .findFirst()
            .orElseThrow(() -> new AssertionError("Produto não encontrado para verificação: " + nomeProduto));
        assertThat(p.getEstoque()).isEqualTo(esperado);
    }

    @Then("o valor total da venda é {double}")
    public void o_valor_total_da_venda_e(Double total) throws Exception {
        resposta.andExpect(jsonPath("$.valorTotal").value(BigDecimal.valueOf(total).doubleValue()));
    }

    // ------------- CENÁRIO NEGATIVO (reaproveitando passos existentes + extras) -------------
    @Then("o sistema rejeita a operação")
    public void o_sistema_rejeita_a_operacao() throws Exception {
        resposta.andExpect(status().is4xxClientError());
    }

    @Then("é exibida a mensagem de erro: {string}")
    public void mensagem_de_erro_estoque(String mensagem) throws Exception {
        // Reuso da asserção específica para contexto de estoque/PDV
        resposta.andExpect(jsonPath("$.message").value(mensagem));
    }
}