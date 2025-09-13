package com.cesarschool.barbearia_backend.vendas.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia_backend.vendas.model.ItemVenda;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, UUID>{

}
