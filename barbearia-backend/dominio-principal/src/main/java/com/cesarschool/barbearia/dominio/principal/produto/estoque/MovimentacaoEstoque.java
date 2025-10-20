package com.cesarschool.barbearia.dominio.principal.produto.estoque;

import java.time.LocalDateTime;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoId;

/**
 * Entidade que representa uma movimentação de estoque (entrada, saída, venda, ajuste).
 * Mantém o histórico de todas as operações realizadas no estoque de produtos.
 * 
 * @author Sistema de Barbearia
 * @version 1.0
 */
public final class MovimentacaoEstoque {

    private MovimentacaoEstoqueId id;
    private ProdutoId produtoId;
    private String nomeProduto;
    private TipoMovimentacao tipo;
    private int quantidade;
    private int estoqueAnterior;
    private int estoqueAtual;
    private LocalDateTime dataHora;
    private String observacao;
    private String usuarioResponsavel;

    /**
     * Construtor completo da movimentação de estoque.
     */
    public MovimentacaoEstoque(
            MovimentacaoEstoqueId id,
            ProdutoId produtoId,
            String nomeProduto,
            TipoMovimentacao tipo,
            int quantidade,
            int estoqueAnterior,
            int estoqueAtual,
            LocalDateTime dataHora,
            String observacao,
            String usuarioResponsavel) {
        
        setId(id);
        setProdutoId(produtoId);
        setNomeProduto(nomeProduto);
        setTipo(tipo);
        setQuantidade(quantidade);
        setEstoqueAnterior(estoqueAnterior);
        setEstoqueAtual(estoqueAtual);
        setDataHora(dataHora);
        setObservacao(observacao);
        setUsuarioResponsavel(usuarioResponsavel);
    }

    /**
     * Construtor simplificado para criar nova movimentação (sem ID).
     */
    public MovimentacaoEstoque(
            ProdutoId produtoId,
            String nomeProduto,
            TipoMovimentacao tipo,
            int quantidade,
            int estoqueAnterior,
            int estoqueAtual,
            String observacao,
            String usuarioResponsavel) {
        
        this(null, produtoId, nomeProduto, tipo, quantidade, estoqueAnterior, 
             estoqueAtual, LocalDateTime.now(), observacao, usuarioResponsavel);
    }

    // ==================== SETTERS COM VALIDAÇÃO ====================

    public void setId(MovimentacaoEstoqueId id) {
        // ID pode ser null na criação (será gerado pelo repositório)
        this.id = id;
    }

    public void setProdutoId(ProdutoId produtoId) {
        Validacoes.validarObjetoObrigatorio(produtoId, "ID do Produto");
        this.produtoId = produtoId;
    }

    public void setNomeProduto(String nomeProduto) {
        Validacoes.validarObjetoObrigatorio(nomeProduto, "Nome do Produto");
        Validacoes.validarStringObrigatoria(nomeProduto, "Nome do Produto");
        this.nomeProduto = nomeProduto;
    }

    public void setTipo(TipoMovimentacao tipo) {
        Validacoes.validarObjetoObrigatorio(tipo, "Tipo de Movimentação");
        this.tipo = tipo;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.quantidade = quantidade;
    }

    public void setEstoqueAnterior(int estoqueAnterior) {
        if (estoqueAnterior < 0) {
            throw new IllegalArgumentException("Estoque anterior não pode ser negativo");
        }
        this.estoqueAnterior = estoqueAnterior;
    }

    public void setEstoqueAtual(int estoqueAtual) {
        if (estoqueAtual < 0) {
            throw new IllegalArgumentException("Estoque atual não pode ser negativo");
        }
        this.estoqueAtual = estoqueAtual;
    }

    public void setDataHora(LocalDateTime dataHora) {
        Validacoes.validarObjetoObrigatorio(dataHora, "Data/Hora da Movimentação");
        this.dataHora = dataHora;
    }

    public void setObservacao(String observacao) {
        // Observação é opcional, mas se informada não pode ser vazia
        if (observacao != null && observacao.trim().isEmpty()) {
            throw new IllegalArgumentException("Observação não pode ser vazia");
        }
        this.observacao = observacao;
    }

    public void setUsuarioResponsavel(String usuarioResponsavel) {
        // Usuário é opcional
        this.usuarioResponsavel = usuarioResponsavel;
    }

    // ==================== GETTERS ====================

    public MovimentacaoEstoqueId getId() {
        return id;
    }

    public ProdutoId getProdutoId() {
        return produtoId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getEstoqueAnterior() {
        return estoqueAnterior;
    }

    public int getEstoqueAtual() {
        return estoqueAtual;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getObservacao() {
        return observacao;
    }

    public String getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    // ==================== MÉTODOS DE NEGÓCIO ====================

    /**
     * Verifica se a movimentação representa uma entrada de estoque.
     */
    public boolean isEntrada() {
        return tipo == TipoMovimentacao.ENTRADA || tipo == TipoMovimentacao.ESTOQUE_INICIAL;
    }

    /**
     * Verifica se a movimentação representa uma saída de estoque.
     */
    public boolean isSaida() {
        return tipo == TipoMovimentacao.SAIDA || tipo == TipoMovimentacao.VENDA;
    }

    /**
     * Retorna a diferença entre estoque atual e anterior.
     */
    public int getDiferencaEstoque() {
        return estoqueAtual - estoqueAnterior;
    }

    @Override
    public String toString() {
        return String.format(
            "MovimentacaoEstoque[id=%s, produto=%s, tipo=%s, quantidade=%d, estoque=%d->%d, data=%s]",
            id != null ? id.getValor() : "NEW",
            nomeProduto,
            tipo.getDescricao(),
            quantidade,
            estoqueAnterior,
            estoqueAtual,
            dataHora
        );
    }
}
