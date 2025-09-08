package com.barbearia.vendas.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.barbearia.vendas.model.ItemVenda;
import com.barbearia.vendas.repository.ItemVendaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemVendaService {
    private final ItemVendaRepository repository;

    public Optional<ItemVenda> findById(UUID id){
        // regras de negócio....
        return repository.findById(id);
    }

    public List<ItemVenda> findAll(){
        // regras de negócio....
        return repository.findAll();
    }

    public ItemVenda save(ItemVenda itemVenda){
        // regras de negócio....
        return repository.save(itemVenda);
    }

    public void delete(ItemVenda itemVenda){
        // regras de negócio....
        repository.delete(itemVenda);
    }
}
