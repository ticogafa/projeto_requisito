package com.cesarschool.barbearia.dominio.vendas.produto;

import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;

public class ProdutoServico {
    private final ProdutoRepositorio repository;
    
    public ProdutoServico(ProdutoRepositorio repository) {
        Validacoes.validarObjetoObrigatorio(repository, "O repositório");
        this.repository = repository;
    }

    public Produto salvar(Produto produto) {
        return repository.salvar(produto);
    }

    public Produto buscarPorId(ProdutoId id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do produto");
        return repository.buscarPorId(id.getValor())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Produto não encontrado com ID: " + id.getValor()
        ));
    }

    public List<Produto> listarTodos() {
        return repository.listarTodos();
    }

    public List<Produto> listarComEstoqueBaixo() {
        return repository.listarProdutosComEstoqueBaixo();
    }

    /**
     * Atualiza um produto existente.
     */
    public Produto atualizar(ProdutoId id, Produto produto) {
        // Verifica se o produto existe
        buscarPorId(id);
        
        return repository.salvar(produto);
    }

    /**
     * Aumenta o estoque de um produto.
     */
    public Produto aumentarEstoque(ProdutoId produtoId, int quantidade) {
        Validacoes.validarObjetoObrigatorio(produtoId, "ID do produto");
        Validacoes.validarInteiroPositivo(quantidade, "Quantidade para aumentar");
        
        Produto produto = buscarPorId(produtoId);
        produto.setEstoque(produto.getEstoque() + quantidade);
        
        return repository.salvar(produto);
    }

    /**
     * Diminui o estoque de um produto (baixa de estoque).
     * Regra de negócio: não pode deixar estoque negativo.
     */
    public Produto baixaEstoque(ProdutoId produtoId, int quantidade) {
        Validacoes.validarObjetoObrigatorio(produtoId, "ID do produto");
        Validacoes.validarInteiroPositivo(quantidade, "Quantidade para baixa");
        
        Produto produto = buscarPorId(produtoId);

        if (produto.getEstoque() < quantidade) {
            throw new IllegalStateException(
                "Estoque insuficiente para baixa. Disponível: " + 
                produto.getEstoque() + ", Solicitado: " + quantidade
            );
        }

        produto.setEstoque(produto.getEstoque() - quantidade);
        return repository.salvar(produto);
    }

    public void remover(ProdutoId id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do produto");
        buscarPorId(id); // Verifica se existe
        repository.remover(id.getValor());
    }
}
