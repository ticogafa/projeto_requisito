package com.cesarschool.barbearia.dominio.principal.itemvenda;

import java.math.BigDecimal;

import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoId;
import com.cesarschool.barbearia.dominio.principal.venda.VendaId;





public final class ItemVenda {

    private ItemVendaId id; // PK

    private VendaId venda;

    private ProdutoId produto; // opcional (só vai estar presente se for tipo Produto)

    private ServicoOferecidoId servicoId; // opcional (só vai estar presente se for tipo servico)

    private String descricao;

    private int quantidade = 1;

    private BigDecimal precoUnitario;

    private BigDecimal precoTotal;

    private TipoVenda tipo;

    public ItemVenda(ItemVendaId id, VendaId venda, ProdutoId produto, ServicoOferecidoId servicoId,
        String descricao, int quantidade, BigDecimal precoUnitario, TipoVenda tipo
        ) {
        setId(id);
        setVenda(venda);
        setProdutoId(produto);
        setServicoId(servicoId);
        setDescricao(descricao);
        setQuantidade(quantidade);
        setPrecoUnitario(precoUnitario);
        setTipo(tipo);
        calcularPrecoTotal();
    }

    public void setId(ItemVendaId id) {
        com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarObjetoObrigatorio(id, "Id do ItemVenda");
        this.id = id;
    }

    public void setVenda(VendaId venda) {
        com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarObjetoObrigatorio(venda, "VendaId");
        this.venda = venda;
    }

    public void setProdutoId(ProdutoId produto) {
        this.produto = produto;
    }

    public void setServicoId(ServicoOferecidoId servicoId) {
        this.servicoId = servicoId;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setQuantidade(int quantidade) {
        com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarValorNaoNegativo(
            BigDecimal.valueOf(quantidade), "Quantidade");
        if (quantidade < 1) {
            throw new IllegalArgumentException("Quantidade deve ser pelo menos 1.");
        }
        this.quantidade = quantidade;
        calcularPrecoTotal();
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarObjetoObrigatorio(precoUnitario, "Preço Unitário");
        com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarValorNaoNegativo(precoUnitario, "Preço Unitário");
        this.precoUnitario = precoUnitario;
        calcularPrecoTotal();
    }

    public void setTipo(TipoVenda tipo) {
        com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarObjetoObrigatorio(tipo, "TipoVenda");
        this.tipo = tipo;
    }

    private void calcularPrecoTotal() {
        if (precoUnitario != null && quantidade > 0) {
            this.precoTotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        } else {
            this.precoTotal = BigDecimal.ZERO;
        }
    }

    public ItemVendaId getId() { return id; }
    public VendaId getVenda() { return venda; }
    public ProdutoId getProdutoId() { return produto; }
    public ServicoOferecidoId getServicoId() { return servicoId; }
    public String getDescricao() { return descricao; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public BigDecimal getPrecoTotal() { return precoTotal; }
    public TipoVenda getTipo() { return tipo; }
}
