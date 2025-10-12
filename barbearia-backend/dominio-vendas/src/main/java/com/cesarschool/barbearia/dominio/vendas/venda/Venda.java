package com.cesarschool.barbearia.dominio.vendas.venda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.marketing.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.marketing.voucher.Voucher;
import com.cesarschool.barbearia.dominio.vendas.itemvenda.ItemVenda;

public final class Venda {
    
    private VendaId id;
    private ClienteId clienteId;
    private LocalDateTime dataVenda;
    private BigDecimal valorTotal;
    private List<ItemVenda> itens;
    private Voucher voucher;
    private String observacoes;

    public Venda(VendaId id, ClienteId clienteId, LocalDateTime dataVenda, 
            List<ItemVenda> itens, Voucher voucher, String observacoes) {
        setId(id);
        setClienteId(clienteId);
        setDataVenda(dataVenda);
        setItens(new ArrayList<>(itens));
        setVoucher(voucher);
        setObservacoes(observacoes);
    }

    public void setId(VendaId id) {
        this.id = id;
    }

    public void setClienteId(ClienteId clienteId) {
        Validacoes.validarObjetoObrigatorio(clienteId, "Cliente da Venda");
        this.clienteId = clienteId;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        Validacoes.validarObjetoObrigatorio(dataVenda, "Data da Venda");
        Validacoes.validarDataNaoFutura(dataVenda, "Data da Venda");
        this.dataVenda = dataVenda;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        Validacoes.validarValorNaoNegativo(valorTotal, "Valor Total da Venda");
        this.valorTotal = valorTotal;
    }

    public void setItens(List<ItemVenda> itens) {
        Validacoes.validarObjetoObrigatorio(itens, "Itens da Venda");
        if (itens.isEmpty()) {
            throw new IllegalArgumentException("A venda deve conter pelo menos um item");
        }
        this.itens = itens;
        this.valorTotal = calcularValorTotal();
        Validacoes.validarValorNaoNegativo(this.valorTotal, "Valor Total da Venda");
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
        this.valorTotal = calcularValorTotal();
        Validacoes.validarValorNaoNegativo(this.valorTotal, "Valor Total da Venda");
    }

    public void setObservacoes(String observacoes) {
        if (observacoes != null) {
            Validacoes.validarTamanhoMaximoString(observacoes, 255, "Observações da Venda");
        }
        this.observacoes = observacoes;
    }

    private BigDecimal calcularValorTotal() {
        BigDecimal subtotal = itens.stream()
            .map(ItemVenda::getPrecoTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (voucher != null) {
            BigDecimal desconto = voucher.getValorDesconto();
            return subtotal.subtract(desconto);
        }
        return subtotal;
    }

    // Getters
    public VendaId getId() { return id; }
    public ClienteId getClienteId() { return clienteId; }
    public LocalDateTime getDataVenda() { return dataVenda; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public List<ItemVenda> getItens() { return Collections.unmodifiableList(itens); }
    public Voucher getVoucher() { return voucher; }
    public String getObservacoes() { return observacoes; }
}