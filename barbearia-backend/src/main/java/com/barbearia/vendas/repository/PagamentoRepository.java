package com.barbearia.vendas.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barbearia.vendas.model.Pagamento;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, UUID>{

}
