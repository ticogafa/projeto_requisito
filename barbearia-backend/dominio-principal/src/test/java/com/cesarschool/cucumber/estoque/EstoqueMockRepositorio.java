package com.cesarschool.cucumber.estoque;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstoqueMockRepositorio {
    
    private Map<String, Produto> produtos = new HashMap<>();
    private Map<String, List<MovimentacaoEstoque>> historicos = new HashMap<>();
    private Map<String, Integer> estoquesMinimos = new HashMap<>();
    private Map<String, Boolean> produtosAtivos = new HashMap<>();
    private List<String> alertas = new ArrayList<>();
    
    public static class Produto {
        private String nome;
        private int estoque;
        private BigDecimal preco;
        private boolean ativo;
        
        public Produto(String nome, int estoque, BigDecimal preco, boolean ativo) {
            this.nome = nome;
            this.estoque = estoque;
            this.preco = preco;
            this.ativo = ativo;
        }
        
        // Getters e setters
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public int getEstoque() { return estoque; }
        public void setEstoque(int estoque) { this.estoque = estoque; }
        public BigDecimal getPreco() { return preco; }
        public void setPreco(BigDecimal preco) { this.preco = preco; }
        public boolean isAtivo() { return ativo; }
        public void setAtivo(boolean ativo) { this.ativo = ativo; }
    }
    
    public static class MovimentacaoEstoque {
        private String tipo;
        private int quantidade;
        private LocalDateTime dataHora;
        private String observacao;
        
        public MovimentacaoEstoque(String tipo, int quantidade, String observacao) {
            this.tipo = tipo;
            this.quantidade = quantidade;
            this.dataHora = LocalDateTime.now();
            this.observacao = observacao;
        }
        
        // Getters
        public String getTipo() { return tipo; }
        public int getQuantidade() { return quantidade; }
        public LocalDateTime getDataHora() { return dataHora; }
        public String getObservacao() { return observacao; }
    }
    
    // Métodos de setup para testes
    public void limparDados() {
        produtos.clear();
        historicos.clear();
        estoquesMinimos.clear();
        produtosAtivos.clear();
        alertas.clear();
    }
    
    // Métodos de produto
    public boolean produtoExiste(String nome) {
        return produtos.containsKey(nome);
    }
    
    public boolean cadastrarProduto(String nome, int estoqueInicial, BigDecimal preco) {
        if (produtoExiste(nome)) {
            return false;
        }
        
        Produto produto = new Produto(nome, estoqueInicial, preco, true);
        produtos.put(nome, produto);
        produtosAtivos.put(nome, true);
        
        // Registrar movimentação inicial
        adicionarMovimentacao(nome, "ENTRADA", estoqueInicial, "Estoque inicial");
        
        return true;
    }
    
    public Produto obterProduto(String nome) {
        return produtos.get(nome);
    }
    
    // Métodos de estoque
    public boolean atualizarEstoque(String nome, int quantidade) {
        Produto produto = produtos.get(nome);
        if (produto == null) return false;
        
        // Reject negative quantity operations directly (invalid operation)
        if (quantidade < 0) {
            return false;
        }
        
        int estoqueAnterior = produto.getEstoque();
        produto.setEstoque(estoqueAnterior + quantidade);
        
        String tipo = quantidade > 0 ? "ENTRADA" : "SAIDA";
        adicionarMovimentacao(nome, tipo, Math.abs(quantidade), "Atualização manual");
        
        verificarEstoqueMinimo(nome);
        
        return true;
    }
    
    public boolean reduzirEstoque(String nome, int quantidade) {
        Produto produto = produtos.get(nome);
        if (produto == null || quantidade < 0) return false;
        
        if (produto.getEstoque() < quantidade) {
            return false; // Estoque insuficiente
        }
        
        produto.setEstoque(produto.getEstoque() - quantidade);
        adicionarMovimentacao(nome, "VENDA", quantidade, "Venda PDV");
        
        verificarEstoqueMinimo(nome);
        
        return true;
    }
    
    // Métodos de status
    public boolean produtoAtivo(String nome) {
        return produtosAtivos.getOrDefault(nome, false);
    }
    
    public boolean desativarProduto(String nome, String motivo) {
        if (!produtoExiste(nome)) return false;
        
        produtosAtivos.put(nome, false);
        Produto produto = produtos.get(nome);
        produto.setAtivo(false);
        
        adicionarMovimentacao(nome, "DESATIVACAO", 0, "Desativado: " + motivo);
        
        return true;
    }
    
    // Métodos de estoque mínimo
    public boolean definirEstoqueMinimo(String nome, int estoqueMinimo) {
        if (!produtoExiste(nome) || estoqueMinimo < 0) return false;
        
        estoquesMinimos.put(nome, estoqueMinimo);
        verificarEstoqueMinimo(nome);
        
        return true;
    }
    
    public void verificarEstoqueMinimo(String nome) {
        Produto produto = produtos.get(nome);
        Integer estoqueMinimo = estoquesMinimos.get(nome);
        
        if (produto != null && estoqueMinimo != null && produto.getEstoque() <= estoqueMinimo) {
            alertas.add("Estoque baixo");
        }
    }
    
    // Métodos de relatório
    public List<Produto> obterProdutosComEstoqueBaixo() {
        List<Produto> produtosBaixo = new ArrayList<>();
        
        for (Map.Entry<String, Produto> entry : produtos.entrySet()) {
            String nome = entry.getKey();
            Produto produto = entry.getValue();
            Integer estoqueMinimo = estoquesMinimos.get(nome);
            
            if (estoqueMinimo != null && produto.getEstoque() < estoqueMinimo) {
                produtosBaixo.add(produto);
            }
        }
        
        return produtosBaixo;
    }
    
    public List<MovimentacaoEstoque> obterHistorico(String nome) {
        return historicos.getOrDefault(nome, new ArrayList<>());
    }
    
    public List<String> obterAlertas() {
        return new ArrayList<>(alertas);
    }
    
    public void limparAlertas() {
        alertas.clear();
    }
    
    // Métodos privados
    private void adicionarMovimentacao(String produto, String tipo, int quantidade, String observacao) {
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(tipo, quantidade, observacao);
        historicos.computeIfAbsent(produto, k -> new ArrayList<>()).add(movimentacao);
    }
}
