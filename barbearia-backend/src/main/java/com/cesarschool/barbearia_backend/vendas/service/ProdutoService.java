package com.cesarschool.barbearia_backend.vendas.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cesarschool.barbearia_backend.vendas.model.Produto;
import com.cesarschool.barbearia_backend.vendas.repository.ProdutoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProdutoService {
    private final ProdutoRepository repository;
    
    public List<Produto> listarComEstoqueBaixo() {
        return repository.findProdutosComEstoqueBaixo();
    }

    public Optional<Produto> findById(UUID id){
        // regras de negócio....
        return repository.findById(id);
    }

    public List<Produto> findAll(){
        // regras de negócio....
        return repository.findAll();
    }

    public Produto save(Produto produto){
        // Validações básicas de cadastro
        if (produto.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        if (produto.getPreco().signum() < 0) {
            throw new IllegalArgumentException("Preço do produto não pode ser negativo");
        }
        if (produto.getEstoque() < 0) {
            throw new IllegalArgumentException("Estoque inicial não pode ser negativo");
        }
        if (produto.getEstoqueMinimo() < 0) {
            throw new IllegalArgumentException("Estoque mínimo não pode ser negativo");
        }
        return repository.save(produto);
    }

    public Produto baixaEstoque(UUID produtoId, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade para baixa deve ser positiva");
        }
        Produto produto = repository.findById(produtoId)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        if (produto.getEstoque() < quantidade) {
            throw new IllegalStateException("Estoque insuficiente para baixa");
        }
        produto.setEstoque(produto.getEstoque() - quantidade);
        return repository.save(produto);
    }

    public void delete(Produto produto){
        // regras de negócio....
        repository.delete(produto);
    }
}
