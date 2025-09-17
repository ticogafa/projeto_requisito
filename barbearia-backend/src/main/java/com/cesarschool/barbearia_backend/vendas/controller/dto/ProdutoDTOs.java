package com.cesarschool.barbearia_backend.vendas.controller.dto;

import java.math.BigDecimal;


import lombok.Data;

public class ProdutoDTOs {

    @Data
    public static class CriarProdutoRequest {
        private String nome;
        private BigDecimal preco;
        private int estoque; // opcional
        private int estoqueMinimo; // opcional
    }

    @Data
    public static class BaixaEstoqueRequest {
        private Integer produtoId;
        private int quantidade;
    }
}
