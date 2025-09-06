package com.barbearia.marketing.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.barbearia.marketing.model.Cliente;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    
}