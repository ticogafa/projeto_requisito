package com.cesarschool.barbearia_backend.marketing.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cesarschool.barbearia_backend.marketing.model.Cliente;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    
}