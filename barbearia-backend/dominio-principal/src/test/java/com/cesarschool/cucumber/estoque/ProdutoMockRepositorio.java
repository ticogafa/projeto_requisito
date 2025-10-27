package com.cesarschool.cucumber.estoque;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.cesarschool.barbearia.dominio.principal.produto.Produto;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoRepositorio;

/**
 * Implementação mock do ProdutoRepositorio para testes.
 * Simula persistência em memória sem necessidade de banco de dados.
 */
public class ProdutoMockRepositorio implements ProdutoRepositorio {
    
    private final Map<Integer, Produto> produtos = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    
    @Override
    public Produto salvar(Produto produto) {
        if (produtos.containsKey(produto.getId())) {
            // Atualiza produto existente
            produtos.put(produto.getId(), produto);
            return produto;
        } else {
            // Novo produto - gera novo ID
            Integer novoId = idGenerator.getAndIncrement();
            Produto novoProduto = new Produto(
                novoId,
                produto.getNome(),
                produto.getEstoque(),
                produto.getPreco(),
                produto.getEstoqueMinimo()
            );
            produtos.put(novoId, novoProduto);
            return novoProduto;
        }
    }
    
    @Override
    public Produto buscarPorId(Integer id) {
        Produto produto = produtos.get(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado com ID: " + id);
        }
        return produto;
    }
    
    @Override
    public List<Produto> listarTodos() {
        return new ArrayList<>(produtos.values());
    }
    
    @Override
    public void remover(Integer id) {
        produtos.remove(id);
    }
    
    @Override
    public List<Produto> findProdutosComEstoqueBaixo() {
        return listarProdutosComEstoqueBaixo();
    }
    
    @Override
    public List<Produto> listarProdutosComEstoqueBaixo() {
        return produtos.values().stream()
            .filter(p -> p.getEstoque() < p.getEstoqueMinimo())
            .collect(Collectors.toList());
    }
    
    /**
     * Limpa todos os dados do repositório.
     * Útil para resetar estado entre testes.
     */
    public void limparDados() {
        produtos.clear();
        idGenerator.set(1);
    }
    
    /**
     * Busca produto por nome (case-insensitive).
     * 
     * @param nome Nome do produto
     * @return Produto encontrado ou null
     */
    public Produto buscarPorNome(String nome) {
        return produtos.values().stream()
            .filter(p -> p.getNome().equalsIgnoreCase(nome))
            .findFirst()
            .orElse(null);
    }
}
