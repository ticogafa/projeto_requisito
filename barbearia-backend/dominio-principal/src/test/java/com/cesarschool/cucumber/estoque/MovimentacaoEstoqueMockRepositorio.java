package com.cesarschool.cucumber.estoque;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.cesarschool.barbearia.dominio.principal.produto.ProdutoId;
import com.cesarschool.barbearia.dominio.principal.produto.estoque.MovimentacaoEstoque;
import com.cesarschool.barbearia.dominio.principal.produto.estoque.MovimentacaoEstoqueId;
import com.cesarschool.barbearia.dominio.principal.produto.estoque.MovimentacaoEstoqueRepositorio;
import com.cesarschool.barbearia.dominio.principal.produto.estoque.TipoMovimentacao;

/**
 * Implementação mock do MovimentacaoEstoqueRepositorio para testes.
 * Simula persistência em memória sem necessidade de banco de dados.
 */
public class MovimentacaoEstoqueMockRepositorio implements MovimentacaoEstoqueRepositorio {
    
    private final Map<Integer, MovimentacaoEstoque> movimentacoes = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    
    @Override
    public MovimentacaoEstoque salvar(MovimentacaoEstoque movimentacao) {
        if (movimentacao.getId() == null) {
            // Nova movimentação - gera ID
            Integer novoId = idGenerator.getAndIncrement();
            MovimentacaoEstoque nova = new MovimentacaoEstoque(
                new MovimentacaoEstoqueId(novoId),
                movimentacao.getProdutoId(),
                movimentacao.getNomeProduto(),
                movimentacao.getTipo(),
                movimentacao.getQuantidade(),
                movimentacao.getEstoqueAnterior(),
                movimentacao.getEstoqueAtual(),
                movimentacao.getDataHora() != null ? movimentacao.getDataHora() : LocalDateTime.now(),
                movimentacao.getObservacao(),
                movimentacao.getUsuarioResponsavel()
            );
            movimentacoes.put(novoId, nova);
            return nova;
        } else {
            // Atualiza movimentação existente
            Integer id = movimentacao.getId().getValor();
            movimentacoes.put(id, movimentacao);
            return movimentacao;
        }
    }
    
    @Override
    public MovimentacaoEstoque buscarPorId(Integer id) {
        MovimentacaoEstoque movimentacao = movimentacoes.get(id);
        if (movimentacao == null) {
            throw new IllegalArgumentException("Movimentação não encontrada com ID: " + id);
        }
        return movimentacao;
    }
    
    @Override
    public List<MovimentacaoEstoque> listarTodos() {
        return new ArrayList<>(movimentacoes.values());
    }
    
    @Override
    public void remover(Integer id) {
        movimentacoes.remove(id);
    }
    
    @Override
    public List<MovimentacaoEstoque> buscarPorProduto(ProdutoId produtoId) {
        return movimentacoes.values().stream()
            .filter(m -> m.getProdutoId() != null && m.getProdutoId().getValor().equals(produtoId.getValor()))
            .sorted(Comparator.comparing(MovimentacaoEstoque::getDataHora).reversed())
            .collect(Collectors.toList());
    }
    
    @Override
    public List<MovimentacaoEstoque> buscarPorProdutoEPeriodo(
            ProdutoId produtoId, 
            LocalDateTime dataInicio, 
            LocalDateTime dataFim) {
        return movimentacoes.values().stream()
            .filter(m -> m.getProdutoId() != null && m.getProdutoId().getValor().equals(produtoId.getValor()))
            .filter(m -> !m.getDataHora().isBefore(dataInicio) && !m.getDataHora().isAfter(dataFim))
            .sorted(Comparator.comparing(MovimentacaoEstoque::getDataHora).reversed())
            .collect(Collectors.toList());
    }
    
    @Override
    public List<MovimentacaoEstoque> buscarPorTipo(TipoMovimentacao tipo) {
        return movimentacoes.values().stream()
            .filter(m -> m.getTipo() == tipo)
            .sorted(Comparator.comparing(MovimentacaoEstoque::getDataHora).reversed())
            .collect(Collectors.toList());
    }
    
    @Override
    public List<MovimentacaoEstoque> buscarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return movimentacoes.values().stream()
            .filter(m -> !m.getDataHora().isBefore(dataInicio) && !m.getDataHora().isAfter(dataFim))
            .sorted(Comparator.comparing(MovimentacaoEstoque::getDataHora).reversed())
            .collect(Collectors.toList());
    }
    
    @Override
    public List<MovimentacaoEstoque> buscarUltimasMovimentacoes(ProdutoId produtoId, int limite) {
        return movimentacoes.values().stream()
            .filter(m -> m.getProdutoId() != null && m.getProdutoId().getValor().equals(produtoId.getValor()))
            .sorted(Comparator.comparing(MovimentacaoEstoque::getDataHora).reversed())
            .limit(limite)
            .collect(Collectors.toList());
    }
    
    /**
     * Limpa todos os dados do repositório.
     * Útil para resetar estado entre testes.
     */
    public void limparDados() {
        movimentacoes.clear();
        idGenerator.set(1);
    }
}
