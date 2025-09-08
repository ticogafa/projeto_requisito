package com.barbearia.marketing.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.barbearia.marketing.model.Cliente;
import com.barbearia.marketing.repository.ClienteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository repository;

    public Optional<Cliente> findById(UUID id){
        // regras de negócio....
        return repository.findById(id);
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
