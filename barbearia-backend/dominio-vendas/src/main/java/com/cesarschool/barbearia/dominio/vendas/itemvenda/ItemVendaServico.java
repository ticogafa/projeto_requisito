package com.cesarschool.barbearia.dominio.vendas.itemvenda;

import java.math.BigDecimal;
import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;

public class ItemVendaServico {
    
    private final ItemVendaRepositorio repositorio;

    public ItemVendaServico(ItemVendaRepositorio repositorio) {
        Validacoes.validarObjetoObrigatorio(repositorio, "O repositório");
        this.repositorio = repositorio;
    }

    public ItemVenda registrar(ItemVenda itemVenda) {
        Validacoes.validarObjetoObrigatorio(itemVenda, "O item de venda");
        return repositorio.salvar(itemVenda);
    }

    public ItemVenda buscarPorId(ItemVendaId id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do item de venda");
        return repositorio.buscarPorId(id.getValor())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Item de venda não encontrado com ID: " + id
        ));
    }

    public List<ItemVenda> listarTodos() {
        return repositorio.listarTodos();
    }

    public ItemVenda atualizar(ItemVendaId id, ItemVenda itemVenda) {
        Validacoes.validarObjetoObrigatorio(id, "ID do item de venda");
        Validacoes.validarObjetoObrigatorio(itemVenda, "O item de venda");
  
        buscarPorId(id);
        
        return repositorio.salvar(itemVenda);
    }

    public BigDecimal calcularTotalItens(List<ItemVenda> itens) {
        Validacoes.validarObjetoObrigatorio(itens, "Lista de itens");
        return itens.stream()
                .map(ItemVenda::getPrecoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void remover(ItemVendaId id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do item de venda");
        buscarPorId(id);
        repositorio.remover(id.getValor());
    }
}
