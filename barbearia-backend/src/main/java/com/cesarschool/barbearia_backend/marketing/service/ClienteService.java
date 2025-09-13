package com.cesarschool.barbearia_backend.marketing.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cesarschool.barbearia_backend.marketing.model.Cliente;
import com.cesarschool.barbearia_backend.marketing.model.Voucher;
import com.cesarschool.barbearia_backend.marketing.repository.ClienteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteService {

    // -- • A cada 100 pontos → R$ 10,00 de desconto
    // -- • Quando o cliente troca pontos, gera um voucher

    // ainda não decidi aonde colocar essa regra?
    // -- • Quando o voucher é utilizado, ele é vinculado à venda (campo voucherId)

    private final ClienteRepository repository;

    public Optional<Cliente> findById(UUID id){
        // regras de negócio....
        return repository.findById(id);
    }

    private Voucher resgatarVoucher(Cliente cliente){
        throw new UnsupportedOperationException("Method not implemented");
    }

    public List<Cliente> findAll(){
        // regras de negócio....
        return repository.findAll();
    }

    public Cliente save(Cliente cliente){
        // regras de negócio....
        return repository.save(cliente);
    }

    public void delete(Cliente cliente){
        // regras de negócio....
        repository.delete(cliente);
    }
}
