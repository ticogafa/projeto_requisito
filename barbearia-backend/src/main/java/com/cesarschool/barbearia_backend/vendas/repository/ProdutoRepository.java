package com.cesarschool.barbearia_backend.vendas.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia_backend.vendas.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer>{
	@Query("SELECT p FROM Produto p WHERE p.estoque <= p.estoqueMinimo")
	List<Produto> findProdutosComEstoqueBaixo();
}
