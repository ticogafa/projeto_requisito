package com.cesarschool.barbearia.dominio.principal.pagamento;

import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;

public class PagamentoServico {
    
    private final PagamentoRepositorio repositorio;

    public PagamentoServico(PagamentoRepositorio repositorio) {
        Validacoes.validarObjetoObrigatorio(repositorio, "O repositório");
        this.repositorio = repositorio;
    }

    /**
     * Registra um novo pagamento.
     * Regras de negócio:
     * - ID é obrigatório
     * - Meio de pagamento é obrigatório
     */
    public Pagamento registrar(Pagamento pagamento) {
        Validacoes.validarObjetoObrigatorio(pagamento, "O pagamento");
        Validacoes.validarObjetoObrigatorio(pagamento.getId(), "ID do pagamento");
        Validacoes.validarObjetoObrigatorio(pagamento.getMeioPagamento(), "Meio de pagamento");
        
        return repositorio.salvar(pagamento);
    }

    public Pagamento salvar(Pagamento pagamento) {
        return repositorio.salvar(pagamento);
    }

    public Pagamento buscarPorId(PagamentoId id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do pagamento");
        return repositorio.buscarPorId(id.getValor())
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
    public Pagamento atualizar(PagamentoId id, Pagamento pagamento) {
        Validacoes.validarObjetoObrigatorio(id, "ID do pagamento");
        Validacoes.validarObjetoObrigatorio(pagamento, "O pagamento");
        Validacoes.validarObjetoObrigatorio(pagamento.getMeioPagamento(), "Meio de pagamento");
        
        // Verifica se o pagamento existe
        buscarPorId(id);
        
        return repositorio.salvar(pagamento);
    }

    public void remover(PagamentoId id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do pagamento");
        buscarPorId(id); // Verifica se existe
        repositorio.remover(id.getValor());
    }
}
