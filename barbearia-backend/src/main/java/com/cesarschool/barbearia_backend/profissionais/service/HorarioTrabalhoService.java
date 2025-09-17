package com.cesarschool.barbearia_backend.profissionais.service;

import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import com.cesarschool.barbearia_backend.profissionais.model.HorarioTrabalho;
import com.cesarschool.barbearia_backend.profissionais.repository.HorarioTrabalhoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HorarioTrabalhoService {
    private final HorarioTrabalhoRepository repository;

    public Optional<HorarioTrabalho> findById(Integer id){
        // regras de negócio....
        return repository.findById(id);
    }

    public List<HorarioTrabalho> findAll(){
        // regras de negócio....
        return repository.findAll();
    }

    public HorarioTrabalho save(HorarioTrabalho horarioTrabalho){
        
        return repository.save(horarioTrabalho);
    }

    public void delete(HorarioTrabalho horarioTrabalho){
        // regras de negócio....
        repository.delete(horarioTrabalho);
    }
}
