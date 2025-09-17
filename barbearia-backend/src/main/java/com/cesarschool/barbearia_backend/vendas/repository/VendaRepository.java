package com.cesarschool.barbearia_backend.vendas.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia_backend.vendas.model.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Integer>{

}
