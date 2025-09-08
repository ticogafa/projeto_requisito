package com.barbearia.vendas.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.barbearia.vendas.model.ItemVenda;
import com.barbearia.vendas.model.Produto;
import com.barbearia.vendas.model.Venda;
import com.barbearia.vendas.repository.VendaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class VendaService {
    private final VendaRepository repository;

    public Optional<Venda> findById(UUID id){
        // regras de negócio....
        return repository.findById(id);
    }

    public List<Venda> findAll(){
        // regras de negócio....
        return repository.findAll();
    }

    public Venda save(Venda venda){

        // -- REGRAS DE NEGÓCIO - VENDA:
        // -- • A Venda de um Produto deve dar baixa no Estoque
        // -- • Um Pagamento deve ser registrado para cada Serviço e Produto vendidos

        // regras de negócio....
        this.verificarPagamento(venda);
        this.atualizarEstoque(venda.getItens());
        return repository.save(venda);
    }

    private void verificarPagamento(Venda venda) {
        throw new UnsupportedOperationException("Unimplemented method 'verificarPagamento'");
    }

    private void atualizarEstoque(List<ItemVenda> itens) {
        throw new UnsupportedOperationException("Unimplemented method 'atualizarEstoque'");
    }

    public void delete(Venda venda){
        // regras de negócio....
        repository.delete(venda);
    }
}
