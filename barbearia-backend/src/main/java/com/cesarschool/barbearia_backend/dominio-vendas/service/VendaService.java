package com.cesarschool.barbearia_backend.vendas.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.cesarschool.barbearia_backend.common.enums.TipoVenda;
import com.cesarschool.barbearia_backend.marketing.model.Cliente;
import com.cesarschool.barbearia_backend.marketing.repository.ClienteRepository;
import com.cesarschool.barbearia_backend.vendas.controller.PdvController.VendaRequest;
import com.cesarschool.barbearia_backend.vendas.model.ItemVenda;
import com.cesarschool.barbearia_backend.vendas.model.Produto;
import com.cesarschool.barbearia_backend.vendas.model.Venda;
import com.cesarschool.barbearia_backend.vendas.repository.ProdutoRepository;
import com.cesarschool.barbearia_backend.vendas.repository.VendaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class VendaService {
    private final VendaRepository repository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Optional<Venda> findById(Integer id){
        return repository.findById(id);
    }

    public List<Venda> findAll(){
        return repository.findAll();
    }

    public Venda save(Venda venda){
        this.verificarPagamento(venda);
        this.atualizarEstoque(venda.getItens());
        Venda saved = repository.save(venda);
        // publica evento para fidelidade se tiver cliente
        if (saved.getCliente() != null) {
            eventPublisher.publishEvent(saved);
        }
        return saved;
    }

    public Venda registrarVendaPDV(VendaRequest request) {
        if (request.getItens() == null || request.getItens().isEmpty()) {
            throw new IllegalArgumentException("Venda deve conter ao menos um item");
        }

        Venda venda = new Venda();
        venda.setDataVenda(LocalDateTime.now());
        venda.setObservacoes(request.getObservacoes());

        if (request.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
            venda.setCliente(cliente);
        }

        List<ItemVenda> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        request.getItens().forEach(ir -> {
            ItemVenda item = new ItemVenda();
            item.setVenda(venda);
            item.setDescricao(ir.getDescricao());
            item.setQuantidade(ir.getQuantidade());
            item.setPrecoUnitario(ir.getPrecoUnitario());
            item.setPrecoTotal(ir.getPrecoUnitario().multiply(BigDecimal.valueOf(ir.getQuantidade())));
            item.setTipo(TipoVenda.valueOf(ir.getTipo()));

            if (item.getTipo() == TipoVenda.PRODUTO) {
                if (ir.getProdutoId() == null) {
                    throw new IllegalArgumentException("produtoId é obrigatório para item de tipo PRODUTO");
                }
                Produto p = produtoRepository.findById(ir.getProdutoId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
                if (p.getEstoque() < item.getQuantidade()) {
                    throw new IllegalStateException("Estoque insuficiente para o produto: " + p.getNome());
                }
                // reserva baixa
                p.setEstoque(p.getEstoque() - item.getQuantidade());
                produtoRepository.save(p);
                item.setProduto(p);
            }
            // Serviço: aqui apenas registra item, sem estoque

            itens.add(item);
        
        });

        for (ItemVenda i : itens) {
            total = total.add(i.getPrecoTotal());
        }
    // garante total não nulo
    venda.setValorTotal(total == null ? BigDecimal.ZERO : total);
        venda.setItens(itens);

        Venda saved = repository.save(venda);
        if (saved.getCliente() != null) {
            eventPublisher.publishEvent(saved);
        }
        return saved;
    }

    private void verificarPagamento(Venda venda) {
        // Placeholder: validação mínima. Em produção, checar pagamentos associados.
        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            throw new IllegalArgumentException("Venda deve conter itens");
        }
    }

    private void atualizarEstoque(List<ItemVenda> itens) {
        // Atualiza estoque de itens de produto
        for (ItemVenda item : itens) {
            if (item.getProduto() != null && item.getQuantidade() > 0) {
                Produto p = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
                if (p.getEstoque() < item.getQuantidade()) {
                    throw new IllegalStateException("Estoque insuficiente para o produto: " + p.getNome());
                }
                p.setEstoque(p.getEstoque() - item.getQuantidade());
                produtoRepository.save(p);
            }
        }
    }

    public void delete(Venda venda){
        repository.delete(venda);
    }
}
