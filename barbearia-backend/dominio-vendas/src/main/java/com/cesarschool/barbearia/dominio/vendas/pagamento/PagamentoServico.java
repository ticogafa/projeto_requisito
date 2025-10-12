package com.cesarschool.barbearia.dominio.vendas.pagamento;

import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;

public class PagamentoServico {
    
    private final PagamentoRepositorio repositorio;

    public PagamentoServico(PagamentoRepositorio repositorio) {
        Validacoes.validarObjetoObrigatorio(repositorio, "O repositório");
        this.repositorio = repositorio;
    }

    public Pagamento salvar(Pagamento pagamento) {
        return repositorio.salvar(pagamento);
    }

    public Pagamento buscarPorId(Integer id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do pagamento");
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Pagamento não encontrado com ID: " + id
        ));
    }

    public List<Pagamento> listarTodos() {
        return repositorio.listarTodos();
    }

    /**
     * Atualiza um pagamento existente.
     */
    public Pagamento atualizar(Integer id, Pagamento pagamento) {
        // Verifica se o pagamento existe
        buscarPorId(id);
        return repositorio.salvar(pagamento);
    }

    public void remover(Integer id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do pagamento");
        buscarPorId(id); // Verifica se existe
        repositorio.remover(id);
    }
}
