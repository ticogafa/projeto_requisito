package com.barbearia.agendamento.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.barbearia.agendamento.model.Agendamento;
import com.barbearia.agendamento.repository.AgendamentoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AgendamentoService {
    private final AgendamentoRepository repository;

    public Optional<Agendamento> findById(UUID id){
        // regras de negócio....
        return repository.findById(id);
    }

        public List<Agendamento> findAll(){
        // regras de negócio....
        return repository.findAll();
    }

    public Agendamento save(Agendamento agendamento){
        // regras de negócio....
        return repository.save(agendamento);
    }

    public void delete(Agendamento agendamento){
        // regras de negócio....
        repository.delete(agendamento);
    }
}
