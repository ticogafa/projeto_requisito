package com.cesarschool.barbearia_backend.vendas.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia_backend.vendas.controller.dto.ProdutoDTOs.CriarProdutoRequest;
import com.cesarschool.barbearia_backend.vendas.model.Produto;
import com.cesarschool.barbearia_backend.vendas.service.ProdutoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<Produto> criar(@RequestBody CriarProdutoRequest req) {
        Produto produto = new Produto(req.getNome(), req.getPreco());
        produto.setEstoque(req.getEstoque());
        produto.setEstoqueMinimo(req.getEstoqueMinimo());
        Produto salvo = produtoService.save(produto);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<Produto>> listarComEstoqueBaixo() {
        return ResponseEntity.ok(produtoService.listarComEstoqueBaixo());
    }

    @PostMapping("/{id}/baixa")
    public ResponseEntity<Produto> baixaManual(@PathVariable("id") UUID id, @RequestParam("quantidade") int quantidade) {
        Produto atualizado = produtoService.baixaEstoque(id, quantidade);
        return ResponseEntity.ok(atualizado);
    }
}
