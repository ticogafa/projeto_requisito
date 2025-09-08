package com.barbearia.marketing.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.barbearia.marketing.model.Voucher;
import com.barbearia.marketing.repository.VoucherRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository repository;

    public Optional<Voucher> findById(UUID id){
        // regras de negócio....
        return repository.findById(id);
    }

    public List<Voucher> findAll(){
        // regras de negócio....
        return repository.findAll();
    }

    public Voucher save(Voucher voucher){
        // regras de negócio....
        return repository.save(voucher);
    }

    public void delete(Voucher voucher){
        // regras de negócio....
        repository.delete(voucher);
    }
}
