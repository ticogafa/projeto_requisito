package com.cesarschool.barbearia_backend.integration.estoque;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.cesarschool.barbearia_backend.vendas.model.Produto;
import com.cesarschool.barbearia_backend.vendas.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional // rollback após cada teste evita necessidade de cleanup manual
class EstoqueTestCasesTest {

	@Autowired private MockMvc mockMvc;
	@Autowired private ProdutoRepository produtoRepository;
	@Autowired private ObjectMapper objectMapper;

	// Sem cleanup explícito: @Transactional garante rollback isolado por teste

	private Produto novoProduto(String nome, int estoque, int estoqueMin, String preco) {
		Produto p = new Produto(nome, new BigDecimal(preco));
		p.setEstoque(estoque);
		p.setEstoqueMinimo(estoqueMin);
		return produtoRepository.save(p);
	}

	private String vendaJson(List<ItemVendaBuilder> itens) {
		ObjectNode root = objectMapper.createObjectNode();
		ArrayNode arr = objectMapper.createArrayNode();
		for (ItemVendaBuilder b : itens) {
			ObjectNode n = objectMapper.createObjectNode();
			n.put("produtoId", b.produtoId);
			n.put("descricao", b.descricao);
			n.put("quantidade", b.quantidade);
			n.put("precoUnitario", b.precoUnitario);
			n.put("tipo", "PRODUTO");
			arr.add(n);
		}
		root.set("itens", arr);
		return root.toString();
	}

	private record ItemVendaBuilder(Integer produtoId, String descricao, int quantidade, double precoUnitario) {}

	@Test
	@DisplayName("[PDV] Registrar venda simples reduz estoque e calcula total")
	void registrarVendaReduzEstoque() throws Exception {
		Produto gel = novoProduto("Gel Fixador", 50, 5, "10.00"); // preco do produto na entidade não interfere no teste

		String json = vendaJson(List.of(new ItemVendaBuilder(gel.getId(), gel.getNome(), 2, 25.90)));

		mockMvc.perform(post("/api/pdv/vendas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.valorTotal").value(51.80));

		Produto atualizado = produtoRepository.findById(gel.getId()).orElseThrow();
		assertThat(atualizado.getEstoque()).isEqualTo(48);
	}

	@Test
	@DisplayName("[PDV] Impedir venda com estoque insuficiente retorna 400 e mensagem")
	void impedirVendaEstoqueInsuficiente() throws Exception {
		Produto p = novoProduto("Pomada Forte", 2, 1, "12.00");
		String json = vendaJson(List.of(new ItemVendaBuilder(p.getId(), p.getNome(), 5, 30.00)));

		mockMvc.perform(post("/api/pdv/vendas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Estoque insuficiente para o produto: Pomada Forte"));

		// estoque não alterado
		assertThat(produtoRepository.findById(p.getId()).orElseThrow().getEstoque()).isEqualTo(2);
	}

	@Test
	@DisplayName("[PDV] Venda sem itens deve falhar (400)")
	void vendaSemItens() throws Exception {
		String json = "{\"itens\":[]}"; // vazio

		mockMvc.perform(post("/api/pdv/vendas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Venda deve conter ao menos um item"));
	}

	@Test
	@DisplayName("[PDV] Venda com múltiplos produtos calcula total correto")
	void vendaMultiplosProdutosCalculaTotal() throws Exception {
		Produto p1 = novoProduto("Navalha", 10, 2, "5.00");
		Produto p2 = novoProduto("Shampoo Especial", 20, 3, "30.00");

		// Itens: (3 * 12.50) + (1 * 45.00) = 82.50
		String json = vendaJson(List.of(
			new ItemVendaBuilder(p1.getId(), p1.getNome(), 3, 12.50),
			new ItemVendaBuilder(p2.getId(), p2.getNome(), 1, 45.00)
		));

		mockMvc.perform(post("/api/pdv/vendas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.valorTotal").value(82.50))
			.andExpect(jsonPath("$.itens.length()" ).value(2));

		assertThat(produtoRepository.findById(p1.getId()).orElseThrow().getEstoque()).isEqualTo(7);
		assertThat(produtoRepository.findById(p2.getId()).orElseThrow().getEstoque()).isEqualTo(19);
	}

	@Test
	@DisplayName("[PDV] Permite venda exaurindo o estoque (vai a zero)")
	void vendaExaurindoEstoque() throws Exception {
		Produto p = novoProduto("Cera Modeladora", 3, 1, "18.00");
		String json = vendaJson(List.of(new ItemVendaBuilder(p.getId(), p.getNome(), 3, 19.90)));

		mockMvc.perform(post("/api/pdv/vendas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.valorTotal").value(59.70));

		assertThat(produtoRepository.findById(p.getId()).orElseThrow().getEstoque()).isEqualTo(0);
	}

	@Nested
	@DisplayName("Validações de payload")
	class PayloadValidation {
		@Test
		@DisplayName("[PDV] ProdutoId ausente em item de tipo PRODUTO gera 400")
		void produtoIdAusente() throws Exception {
			// Monta item sem produtoId
			ObjectNode root = objectMapper.createObjectNode();
			ArrayNode arr = objectMapper.createArrayNode();
			ObjectNode item = objectMapper.createObjectNode();
			item.put("descricao", "Item inválido");
			item.put("quantidade", 1);
			item.put("precoUnitario", 10.0);
			item.put("tipo", "PRODUTO");
			arr.add(item);
			root.set("itens", arr);

			mockMvc.perform(post("/api/pdv/vendas")
					.contentType(MediaType.APPLICATION_JSON)
					.content(root.toString()))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("produtoId é obrigatório para item de tipo PRODUTO"));
		}
	}
}
