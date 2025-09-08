package com.barbearia.profissionais.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.barbearia.profissionais.model.ServicoOferecido;
import com.barbearia.profissionais.repository.ServicoOferecidoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ServicoOferecidoService {
    private final ServicoOferecidoRepository repository;

    public Optional<ServicoOferecido> findById(UUID id){
        // regras de negócio....
        return repository.findById(id);
    }

    public List<ServicoOferecido> findAll(){
        // regras de negócio....
        return repository.findAll();
    }

    public ServicoOferecido save(ServicoOferecido servicoOferecido){
        // regras de negócio....
        return repository.save(servicoOferecido);
    }

    public void delete(ServicoOferecido servicoOferecido){
        // regras de negócio....
        repository.delete(servicoOferecido);
    }
}
