package com.cesarschool.barbearia.dominio.principal.produto;

import java.math.BigDecimal;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;

public final class Produto {

    private Integer id;
    private String nome;
    private int estoque = 0;
    private BigDecimal preco;
    private int estoqueMinimo = 0;

    public Produto(Integer id, String nome, int estoque, BigDecimal preco, int estoqueMinimo) {
        setId(id);
        setNome(nome);
        setEstoque(estoque);
        setPreco(preco);
        setEstoqueMinimo(estoqueMinimo);
    }

    public void setId(Integer id) {
        Validacoes.validarObjetoObrigatorio(id, "Id do Produto");
        this.id = id;
    }

    public void setNome(String nome) {
        Validacoes.validarObjetoObrigatorio(nome, "Nome do Produto");
        this.nome = nome;
    }

    public void setEstoque(int estoque) {
        Validacoes.validarValorNaoNegativo(BigDecimal.valueOf(estoque), "Estoque");
        this.estoque = estoque;
    }

    public void setPreco(BigDecimal preco) {
        Validacoes.validarObjetoObrigatorio(preco, "Preço do Produto");
        Validacoes.validarValorNaoNegativo(preco, "Preço do Produto");
        this.preco = preco;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        Validacoes.validarValorNaoNegativo(BigDecimal.valueOf(estoqueMinimo), "Estoque Mínimo");
        this.estoqueMinimo = estoqueMinimo;
    }

    public Integer getId() { return id; }
    public String getNome() { return nome; }
    public int getEstoque() { return estoque; }
    public BigDecimal getPreco() { return preco; }
    public int getEstoqueMinimo() { return estoqueMinimo; }
}