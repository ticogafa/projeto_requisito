package com.barbearia.vendas.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.barbearia.vendas.model.Produto;
import com.barbearia.vendas.repository.ProdutoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProdutoService {
    private final ProdutoRepository repository;

    public Optional<Produto> findById(UUID id){
        // regras de negócio....
        return repository.findById(id);
    }

    public List<Produto> findAll(){
        // regras de negócio....
        return repository.findAll();
    }

    public Produto save(Produto produto){
        // regras de negócio....
        // -- • Não pode ser feita venda de um produto sem estoque
        this.verificarEstoque(produto);
        return repository.save(produto);
    }

    private void verificarEstoque(Produto produto) {
        throw new UnsupportedOperationException("Unimplemented method 'verificarEstoque'");
    }

    public void delete(Produto produto){
        // regras de negócio....
        repository.delete(produto);
    }
}
