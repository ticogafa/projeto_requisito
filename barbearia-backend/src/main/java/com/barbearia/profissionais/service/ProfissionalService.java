package com.barbearia.profissionais.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.barbearia.profissionais.model.Profissional;
import com.barbearia.profissionais.repository.ProfissionalRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfissionalService {
    private final ProfissionalRepository repository;

    public Optional<Profissional> findById(UUID id){
        // regras de negócio....
        return repository.findById(id);
    }

    public List<Profissional> findAll(){
        // regras de negócio....
        return repository.findAll();
    }

    public Profissional save(Profissional profissional){
        // regras de negócio....
        return repository.save(profissional);
    }

    public void delete(Profissional profissional){
        // regras de negócio....
        repository.delete(profissional);
    }
}
