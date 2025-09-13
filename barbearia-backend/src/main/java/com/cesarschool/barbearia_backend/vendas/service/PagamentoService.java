package com.cesarschool.barbearia_backend.vendas.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cesarschool.barbearia_backend.vendas.model.Pagamento;
import com.cesarschool.barbearia_backend.vendas.repository.PagamentoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PagamentoService {
    private final PagamentoRepository repository;

    public Optional<Pagamento> findById(UUID id){
        // regras de negócio....
        return repository.findById(id);
    }

    public List<Pagamento> findAll(){
        // regras de negócio....
        return repository.findAll();
    }

    public Pagamento save(Pagamento pagamento){
        // regras de negócio....
        return repository.save(pagamento);
    }

    public void delete(Pagamento pagamento){
        // regras de negócio....
        repository.delete(pagamento);
    }
}
